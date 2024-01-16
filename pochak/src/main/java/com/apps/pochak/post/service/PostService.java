package com.apps.pochak.post.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.LikeAlarm;
import com.apps.pochak.alarm.domain.PostRequestAlarm;
import com.apps.pochak.alarm.repository.AlarmRepository;
import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.repository.CommentRepository;
import com.apps.pochak.common.AwsS3Service;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponseStatus;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.dto.LikedUsersResDto;
import com.apps.pochak.post.dto.PostDetailResDto;
import com.apps.pochak.post.dto.PostUploadRequestDto;
import com.apps.pochak.post.dto.PostUploadResDto;
import com.apps.pochak.post.repository.PostRepository;
import com.apps.pochak.publish.domain.Publish;
import com.apps.pochak.publish.repository.PublishRepository;
import com.apps.pochak.tag.domain.Tag;
import com.apps.pochak.tag.repository.TagRepository;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.common.BaseResponseStatus.*;
import static com.apps.pochak.common.Status.*;
import static com.apps.pochak.post.dto.LikedUsersResDto.LikedUser;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AlarmRepository alarmRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final DynamoDBMapper mapper;
    private final PublishRepository publishRepository;
    private final TagRepository tagRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public PostUploadResDto savePost(PostUploadRequestDto requestDto, String loginUserHandle) throws BaseException {
        try {
            if (requestDto.getTaggedUserHandles().isEmpty()) {
                throw new BaseException(NULL_TAGGED_USER);
            } else if (requestDto.getPostImage().isEmpty()) {
                throw new BaseException(NULL_IMAGE);
            }
            User postOwner = userRepository.findUserByUserHandle(loginUserHandle);
            String postImageUrl = awsS3Service.upload(requestDto.getPostImage(), "post");
            Post post = requestDto.toEntity(postOwner, postImageUrl);
            Post savedPost = postRepository.savePost(post);

            // save publish
            Publish publish = new Publish(postOwner, savedPost);
            publishRepository.save(publish);

            List<PostRequestAlarm> requestAlarms = post.getTaggedUserHandles().stream().map(
                    taggedUserHandle -> {
                        PostRequestAlarm postRequestAlarm = new PostRequestAlarm(taggedUserHandle, postOwner, post);
                        return postRequestAlarm;
                    }).collect(Collectors.toList());
            mapper.batchSave(requestAlarms);

            return new PostUploadResDto(savedPost);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(e);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // PostResDto에서 사용할 parentComment조회 함수
    public List getParentComments(Post post) throws BaseException {
        List parentComments = post.getParentCommentSKs().stream().map(
                        commentSK -> {
                            try {
                                // commentRepository에 가서 comment 가져오기
                                return commentRepository.findCommentByCommentSK(post.getPostPK(), commentSK);
                            } catch (Exception e) {
                                throw new RuntimeException("해당 Post의 ParentComments List에 더미 CommentId 데이터가 없는지 확인해주세요");
                            }
                        })
                .collect(Collectors.toList());

        return parentComments;
    }

    public PostDetailResDto getPostDetail(String postPK, String loginUserHandle) throws BaseException {
        // PK로 찾기
        try {
            Post postByPostPK = postRepository.findPostByPostPK(postPK);
            checkValid(postByPostPK, loginUserHandle);
            User loginUser = userRepository.findUserByUserHandle(loginUserHandle);
            User owner = userRepository.findUserByUserHandle(postByPostPK.getOwnerHandle());
            boolean isFollow = owner.getFollowerUserHandles().contains(loginUserHandle);

            Comment randomComment;
            if (postByPostPK.getParentCommentSKs().size() != 0) {
                randomComment = commentRepository.findRandomCommentsByPostPK(postPK);
                return new PostDetailResDto(postByPostPK, owner, loginUser, isFollow, randomComment);
            } else
                return new PostDetailResDto(postByPostPK, owner, loginUser, isFollow);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public LikedUsersResDto getUsersLikedPost(String postPK, String loginUserHandle) throws BaseException {
        try {
            Post likedPost = postRepository.findPostByPostPK(postPK);
            checkPublic(likedPost);

            List<String> likeUserHandles = likedPost.getLikeUserHandles();
            List<User> users = userRepository.batchGetUsers(likeUserHandles);
            User loginUser = userRepository.findUserByUserHandle(loginUserHandle);

            List<LikedUser> likedUserList = new ArrayList<>();
            if (users != null) {
                likedUserList = users.stream().map(
                        user -> new LikedUser(user, loginUser)
                ).collect(Collectors.toList());
            }

            return new LikedUsersResDto(likedUserList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(e);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public BaseResponseStatus likePost(String postPK, String loginUserHandle) throws BaseException {
        try {
            Post postByPostPK = postRepository.findPostByPostPK(postPK);
            checkPublic(postByPostPK);

            if (postByPostPK.getOwnerHandle().equals(loginUserHandle)) {
                return POST_OWNER_LIKE;
            }

            // 중복 검사
            boolean contain = postByPostPK.getLikeUserHandles().contains(loginUserHandle);
            if (!contain) {
                postByPostPK.getLikeUserHandles().add(loginUserHandle);
                User likeUser = userRepository.findUserByUserHandle(loginUserHandle);
                LikeAlarm likeAlarm = new LikeAlarm(postByPostPK.getOwnerHandle(), likeUser, postByPostPK);
                alarmRepository.saveAlarm(likeAlarm);
            }
            else
                postByPostPK.getLikeUserHandles().remove(loginUserHandle);
            postRepository.savePost(postByPostPK);

            return (!contain) ? SUCCESS_LIKE : CANCEL_LIKE;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public BaseResponseStatus deletePost(String postPK, String loginUserHandle) throws BaseException {
        try {
            Post deletePost = postRepository.findPostByPostPK(postPK);
            checkValid(deletePost, loginUserHandle);
            if (!loginUserHandle.equals(deletePost.getOwnerHandle())) {
                throw new BaseException(NOT_YOUR_POST);
            }

            deletePost.setStatus(DELETED);
            postRepository.savePost(deletePost);

            setDeleteRelatedAlarmByPost(deletePost);
            setDeleteRelatedTagByPost(deletePost);
            setDeleteRelatedPublishByPost(deletePost);
            setDeleteRelatedCommentByPost(deletePost);
            return SUCCESS;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void setDeleteRelatedAlarmByPost(Post deletePost) {
        List<Alarm> alarms
                = alarmRepository.findAllPublicAlarmsWithUserHandleAndPostPK(deletePost.getOwnerHandle(), deletePost.getPostPK());

        alarmRepository.deleteAlarms(alarms);
    }

    private void setDeleteRelatedTagByPost(Post post) {
        List<Tag> deleteTagList
                = tagRepository.findPublicAndPrivateTagsByUserHandleAndPostPK(post.getOwnerHandle(), post.getPostPK());

        tagRepository.deletePublicAndPrivatePosts(deleteTagList);
    }

    private void setDeleteRelatedPublishByPost(Post post) {
        List<Publish> deletePublishList
                = publishRepository.findPublicAndPrivatePublishWithUserHandleAndPostPK(post.getOwnerHandle(), post.getPostPK());

        publishRepository.deletePublicAndPrivatePublish(deletePublishList);

    }

    private void setDeleteRelatedCommentByPost(Post post) {
        List<Comment> comments = commentRepository.findAllPublicCommentsByPostPK(post.getPostPK());

        commentRepository.deleteComments(comments);
    }

    private void checkValid(Post post, String loginUserHandle) throws BaseException {
        if (post.getStatus().equals(PRIVATE)) {
            if (!(post.getOwnerHandle().equals(loginUserHandle) ||
                    post.getTaggedUserHandles().contains(loginUserHandle))) {
                throw new BaseException(NOT_ALLOW_POST);
            }
        } else if (post.getStatus().equals(DELETED)) {
            throw new BaseException(DELETED_POST);
        }
    }

    private void checkPublic(Post post) throws BaseException {
        if (!post.getStatus().equals(PUBLIC)) {
            throw new BaseException(NOT_ALLOW_POST);
        }
    }
}
