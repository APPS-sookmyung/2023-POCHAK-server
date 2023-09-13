package com.apps.pochak.user.service;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
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
import java.util.stream.Collectors;

import static com.apps.pochak.common.BaseResponseStatus.*;
import static com.apps.pochak.publish.repository.PublishRepository.PublishData;
import static com.apps.pochak.tag.repository.TagRepository.TagData;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PublishRepository publishRepository;

    @Transactional
    public User saveUser(User user) {
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
                publishData = publishRepository.findAllPublishWithUserHandle(userHandle, exclusiveStartKey);
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
            List<User> followers = userByUserPK.getFollowerUserHandles().stream().map(
                            followerHandle -> {
                                try {
                                    return userRepository.findUserByUserHandle(followerHandle);
                                } catch (Exception e) {
                                    throw new RuntimeException("해당 User의 Follower List에 더미 userID 데이터가 있는지 확인하세요");
                                }
                            })
                    .collect(Collectors.toList());
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
            List<User> followings = userRepository.findUserByUserHandle(userHandle).getFollowingUserHandles().stream().map(
                    followingHandle -> {
                        try {
                            return userRepository.findUserByUserHandle(followingHandle);
                        } catch (BaseException e) {
                            throw new RuntimeException("해당 User의 Following List에 더미 userID 데이터가 있는지 확인하세요");
                        }
                    }
            ).collect(Collectors.toList());
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

            userWithUserHandle.updateUser(
                    requestDto.getProfileImgUrl(),
                    requestDto.getName(),
                    requestDto.getMessage());
            User savedUser = userRepository.saveUser(userWithUserHandle);

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
            return userRepository.followOrCancelByIsFollow(userHandle, loginUserHandle, isFollow);
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
            return userRepository.followOrCancelByIsFollow(loginUserHandle, userHandle, isFollow);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
