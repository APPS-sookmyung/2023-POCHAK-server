package com.apps.pochak.comment.service;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.dto.CommentDeleteRequestDto;
import com.apps.pochak.comment.dto.CommentResDto;
import com.apps.pochak.comment.dto.CommentUploadRequestDto;
import com.apps.pochak.comment.repository.CommentRepository;
import com.apps.pochak.common.BaseEntity;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponseStatus;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.repository.PostRepository;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.apps.pochak.common.BaseResponseStatus.*;
import static com.apps.pochak.common.Status.DELETED;
import static com.apps.pochak.common.Status.PUBLIC;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public BaseResponseStatus deleteComment(String postPK, String loginUserHandle, CommentDeleteRequestDto requestDto) throws BaseException {
        try {
            String deleteCommentSK = requestDto.getDeletedCommentSK();
            Comment deleteComment = commentRepository.findCommentByCommentSK(postPK, deleteCommentSK);

            // 권한 확인
            if (!loginUserHandle.equals(deleteComment.getCommentUserHandle())) {
                throw new BaseException(BaseResponseStatus.NOT_YOUR_COMMENT);
            }

            if (requestDto.getDeletedParentCommentSK() == null) {
                // parent
                deleteChildComments(deleteComment);
            } else {
                checkRecentCommentOfParentCommentAndDelete(postPK, requestDto);
            }
            deleteComment.setStatus(DELETED);
            commentRepository.saveComment(deleteComment);

            return SUCCESS;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void checkRecentCommentOfParentCommentAndDelete(String postPK, CommentDeleteRequestDto requestDto) throws BaseException {
        Comment parentComment = commentRepository.findCommentByCommentSK(postPK, requestDto.getDeletedParentCommentSK());
        if (parentComment.getRecentChildCommentSK().equals(requestDto.getDeletedCommentSK())) {
            Comment recentComment = commentRepository.findRecentChildCommentOfParentComment(parentComment.getUploadedDate(), postPK);
            User owner = userRepository.findUserByUserHandle(recentComment.getCommentUserHandle());
            parentComment.setRecentChildComment(recentComment, owner);
        }
        commentRepository.saveComment(parentComment);
    }

    // 댓글 조회
    public CommentResDto getAllComments(String postPK, String loginUserHandle) throws BaseException {
        try {
            User loginUser = userRepository.findUserByUserHandle(loginUserHandle);
            List<Comment> parentCommentsByPostPK = commentRepository.findParentCommentsByPostPK(postPK);
            return new CommentResDto(loginUser, parentCommentsByPostPK);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void deleteChildComments(Comment deleteComment) throws BaseException {
        List<Comment> deleteChildCommentList
                = commentRepository.findChildCommentByParentCommentSKAndPostPK(deleteComment.getUploadedDate(), deleteComment.getPostPK());
        commentRepository.deleteComments(deleteChildCommentList);
    }

    @Transactional
    public BaseResponseStatus commentUpload(String postPK,
                                            CommentUploadRequestDto requestDto,
                                            String loginUserHandle) throws BaseException {
        try {
            User loginUser = userRepository.findUserByUserHandle(loginUserHandle);
            Post commentedPost = postRepository.findPostByPostPK(postPK);
            if (requestDto.getParentCommentSK() == null) {
                saveParentComment(commentedPost, loginUser, requestDto);
            } else {
                saveChildComment(commentedPost, loginUser, requestDto);
            }
            return SUCCESS;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            System.err.println(e);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void saveParentComment(Post commentedPost, User loginUser, CommentUploadRequestDto requestDto) throws BaseException {
        String uploadedDate = "COMMENT#" + "PARENT#" + LocalDateTime.now();
        commentedPost.getParentCommentSKs().add(uploadedDate);
        postRepository.savePost(commentedPost);
        Comment comment = requestDto.toEntity(commentedPost, loginUser, uploadedDate);
        comment.setStatus(PUBLIC);
        commentRepository.saveComment(comment);
    }

    public void saveChildComment(Post commentedPost, User loginUser, CommentUploadRequestDto requestDto) throws BaseException {
        String uploadedDate = "COMMENT#" + "CHILD#" + LocalDateTime.now();
        Comment parentComment = commentRepository.findCommentByCommentSK(commentedPost.getPostPK(), requestDto.getParentCommentSK());
        checkPublic(parentComment);
        parentComment.getChildCommentSKs().add(uploadedDate);

        Comment comment = requestDto.toEntity(commentedPost, loginUser, uploadedDate);
        comment.setStatus(PUBLIC);

        parentComment.setRecentChildComment(comment, loginUser);

        commentRepository.saveComment(parentComment);
        commentRepository.saveComment(comment);
    }

    private void checkPublic(BaseEntity data) throws BaseException {
        if (!data.getStatus().equals(PUBLIC)) {
            throw new BaseException(NOT_ALLOW_POST);
        }
    }
}
