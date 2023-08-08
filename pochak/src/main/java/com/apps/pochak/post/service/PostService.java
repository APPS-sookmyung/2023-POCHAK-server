package com.apps.pochak.post.service;

import com.apps.pochak.comment.repository.CommentRepository;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.dto.PostResDto;
import com.apps.pochak.post.dto.PostUploadRequestDto;
import com.apps.pochak.post.dto.PostUploadResDto;
import com.apps.pochak.post.repository.PostRepository;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public PostUploadResDto savePost(PostUploadRequestDto requestDto, String loginUserHandle) throws BaseException {
        try {
            User loginUser = userRepository.findUserByUserHandle(loginUserHandle);
//            Post post = requestDto.toEntity(loginUser.get);
            postRepository.savePost(post);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // PostResDto에서 사용할 parentComment조회 함수
    public List getParentComments(Post post) throws BaseException {
        List parentComments = post.getParentComments().stream().map(
                        commentId -> {
                            try {
                                // commentRepository에 가서 comment 가져오기
                                return commentRepository.findCommentByCommentId(commentId).getContent();
                            } catch (Exception e) {
                                throw new RuntimeException("해당 Post의 ParentComments List에 더미 CommentId 데이터가 없는지 확인해주세요");
                            }
                        })
                .collect(Collectors.toList());

        return parentComments;
    }

    public PostResDto getPostDetail(String postPK) throws BaseException {
        // PK로 찾기
        try {
            Post postByPostPK = postRepository.findPostWithPostPK(postPK);
            return new PostResDto(postByPostPK, this); //** here
        } catch (BaseException e) {
            throw e;
        }
    }
}
