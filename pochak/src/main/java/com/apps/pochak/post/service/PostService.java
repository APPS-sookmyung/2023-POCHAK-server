package com.apps.pochak.post.service;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.repository.CommentRepository;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.dto.LikedUsersResDto;
import com.apps.pochak.post.dto.PostDetailResDto;
import com.apps.pochak.post.dto.PostUploadRequestDto;
import com.apps.pochak.post.dto.PostUploadResDto;
import com.apps.pochak.post.repository.PostRepository;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostUploadResDto savePost(PostUploadRequestDto requestDto, String loginUserHandle) throws BaseException {
        try {
            if (requestDto.getTaggedUserHandles().isEmpty()) {
                throw new BaseException(NULL_TAGGED_USER);
            } else if (requestDto.getPostImageUrl().isBlank()) {
                throw new BaseException(NULL_IMAGE);
            }
            User postOwner = userRepository.findUserByUserHandle(loginUserHandle);
            List<User> taggedUsers = requestDto.getTaggedUserHandles().stream().map(
                    userHandle -> {
                        try {
                            return userRepository.findUserByUserHandle(userHandle);
                        } catch (BaseException e) {
                            throw new RuntimeException(e);
                        }
                    }
            ).collect(Collectors.toList());

            Post post = requestDto.toEntity(postOwner, taggedUsers);
            return new PostUploadResDto(postRepository.savePost(post));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
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
            User owner = userRepository.findUserByUserHandle(postByPostPK.getOwnerHandle());
            boolean isFollow = owner.getFollowerUserHandles().contains(loginUserHandle);
            Comment randomComment = commentRepository.findRandomCommentsByPostPK(postPK);
            return new PostDetailResDto(postByPostPK, isFollow, randomComment);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public LikedUsersResDto getUsersLikedPost(String postPK,String loginUserHandle) throws BaseException {
        try {
            Post likedPost=postRepository.findPostByPostPK(postPK);
            List<LikedUsersResDto.likedUser> likedUsers=likedPost.getLikeUserHandles().stream().map(
                    userHandle->{
                        try{
                            User likedpostUser=userRepository.findUserByUserHandle(userHandle);
                            String profileImage=likedpostUser.getProfileImage();
                            String name=likedpostUser.getName();
                            boolean follow=true; // test : userHandle 넘겨서 내가 팔로우하는지 체크 loginUserHandle
                            LikedUsersResDto.likedUser likedUser=new LikedUsersResDto.likedUser(userHandle,profileImage,name,follow);
                            return likedUser;

                        }catch (BaseException e){
                            throw new RuntimeException(e);
                        }
                    }
            ).collect(Collectors.toList());
            return (LikedUsersResDto) likedUsers;
        }catch (BaseException e){
            throw e;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
