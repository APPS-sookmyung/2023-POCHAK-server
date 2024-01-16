package com.apps.pochak.user.service;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.apps.pochak.alarm.domain.FollowAlarm;
import com.apps.pochak.alarm.repository.AlarmRepository;
import com.apps.pochak.comment.repository.CommentRepository;
import com.apps.pochak.common.AwsS3Service;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.post.repository.PostRepository;
import com.apps.pochak.publish.repository.PublishRepository;
import com.apps.pochak.tag.domain.Tag;
import com.apps.pochak.tag.repository.TagRepository;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.dto.*;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.apps.pochak.common.BaseResponseStatus.*;
import static com.apps.pochak.common.Status.PUBLIC;
import static com.apps.pochak.publish.repository.PublishRepository.PublishData;
import static com.apps.pochak.tag.repository.TagRepository.TagData;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PublishRepository publishRepository;
    private final AlarmRepository alarmRepository;
    private final CommentRepository commentRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public User saveUser(User user) {
        user.setStatus(PUBLIC);
        return userRepository.saveUser(user);
    }

    public UserProfileResDto getUserProfile(String userHandle, String loginUserHandle,
                                            Map<String, AttributeValue> exclusiveStartKey) throws BaseException {
        try {
            if (userHandle.isBlank()) {
                throw new BaseException(NULL_USER_HANDLE);
            } else if (loginUserHandle.isBlank()) {
                throw new BaseException(INVALID_LOGIN_INFO);
            }

            User user = userRepository.findUserByUserHandle(userHandle);
            User loginUser = userRepository.findUserByUserHandle(loginUserHandle);

            TagData publicTagsByUserHandle = tagRepository.findPublicTagsByUserHandle(userHandle, exclusiveStartKey);
            List<Tag> tags = publicTagsByUserHandle.getResult();
            Boolean isFollow = userRepository.isFollow(userHandle, loginUserHandle);

            return UserProfileResDto.builder()
                    .user(user)
                    .loginUser(loginUser)
                    .tags(tags)
                    .isFollow(isFollow)
                    .exclusiveStartKey(publicTagsByUserHandle.getExclusiveStartKey())
                    .build();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(e);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public UserPublishResDto getUploadPosts(String userHandle, String loginUserHandle,
                                            Map<String, AttributeValue> exclusiveStartKey) throws BaseException {
        try {
            if (userHandle.isBlank()) {
                throw new BaseException(NULL_USER_HANDLE);
            } else if (loginUserHandle.isBlank()) {
                throw new BaseException(INVALID_LOGIN_INFO);
            }

            PublishData publishData;
            if (userHandle.equals(loginUserHandle)) { // 자신의 Publish 조회
                publishData = publishRepository.findPublicAndPrivatePublishWithUserHandle(userHandle, exclusiveStartKey);
            } else { // 다른 사람의 Publish 조회
                publishData = publishRepository.findOnlyPublicPublishWithUserHandle(userHandle, exclusiveStartKey);
            }

            return new UserPublishResDto(publishData.getResult(), publishData.getExclusiveStartKey());
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // TODO: Pagination 필요할 수도 있음.
    public UserFollowersResDto getUserFollowers(String handle) throws BaseException {
        try {
            if (handle.isBlank()) {
                throw new BaseException(NULL_USER_HANDLE);
            }
            User userByUserPK = userRepository.findUserByUserHandle(handle);

            List<String> userPKList = List.copyOf(userByUserPK.getValidFollowerSet());
            List<User> followers = userRepository.batchGetUsers(userPKList);
            return new UserFollowersResDto(followers);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // TODO: Pagination 필요할 수도 있음.
    public UserFollowingsResDto getUserFollowings(String userHandle) throws BaseException {
        try {
            if (userHandle.isBlank()) {
                throw new BaseException(NULL_USER_HANDLE);
            }
            User userByUserPK = userRepository.findUserByUserHandle(userHandle);

            List<String> userPKList = List.copyOf(userByUserPK.getValidFollowingSet());
            List<User> followings = userRepository.batchGetUsers(userPKList);

            return new UserFollowingsResDto(followings);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public UserUpdateResDto updateUserProfile(String updatedUserHandle, UserUpdateRequestDto requestDto) throws BaseException {
        try {
            User userWithUserHandle = userRepository.findUserByUserHandle(updatedUserHandle);

            // profileImage와 한줄 소개는 null 값 가능하게 설정.
            if (requestDto.getName().isBlank()) {
                throw new BaseException(NULL_USER_NAME);
            }

            awsS3Service.deleteFileFromS3(userWithUserHandle.getProfileImage());
            String profileImageUrl = awsS3Service.upload(requestDto.getProfileImgUrl(), "profile");

            userWithUserHandle.updateUser(
                    profileImageUrl,
                    requestDto.getName(),
                    requestDto.getMessage());
            User savedUser = userRepository.saveUser(userWithUserHandle);

            // duplicate data
            updateComments(userWithUserHandle);

            return UserUpdateResDto.builder()
                    .profileImgUrl(savedUser.getProfileImage())
                    .name(savedUser.getName())
                    .handle(savedUser.getHandle())
                    .message(savedUser.getMessage())
                    .build();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void updateComments(User user) {
        commentRepository.updateOwnerProfileImageOfComments(user);
    }

    public Boolean checkHandleDuplicate(String handle) throws BaseException {
        try {
            userRepository.findUserByUserHandle(handle);
            return true; // 중복됨
        } catch (BaseException e) {
            if (e.getStatus().equals(INVALID_USER_HANDLE)) {
                return false; // 중복되지 않음
            }
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public String followUser(String userHandle, String loginUserHandle) throws BaseException {
        try {
            User loginUser = userRepository.findUserByUserHandle(loginUserHandle); // handle 유효 검사
            User followedUser = userRepository.findUserByUserHandle(userHandle);

            boolean isFollow = userRepository.isFollow(userHandle, loginUserHandle);

            if (!isFollow) {
                FollowAlarm followAlarm = new FollowAlarm(followedUser, loginUser);
                alarmRepository.saveAlarm(followAlarm);
            }

            return userRepository.followOrCancelByIsFollow(followedUser, loginUser, isFollow);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public String deleteFollower(String userHandle, String loginUserHandle) throws BaseException {
        try {
            User loginUser = userRepository.findUserByUserHandle(loginUserHandle); // handle 유효 검사
            User followedUser = userRepository.findUserByUserHandle(userHandle);

            boolean isFollow = userRepository.isFollow(loginUserHandle, userHandle);
            if (!isFollow) {
                throw new BaseException(INVALID_FOLLOWER);
            }
            return userRepository.followOrCancelByIsFollow(loginUser, followedUser, true);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
