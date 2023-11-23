package com.apps.pochak.comment.service;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.dto.CommentDeleteRequestDto;
import com.apps.pochak.comment.dto.CommentUploadRequestDto;
import com.apps.pochak.comment.repository.CommentRepository;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponseStatus;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.repository.PostRepository;
import com.apps.pochak.publish.repository.PublishRepository;
import com.apps.pochak.tag.repository.TagRepository;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.apps.pochak.common.BaseResponseStatus.DATABASE_ERROR;
import static com.apps.pochak.common.BaseResponseStatus.SUCCESS;
import static com.apps.pochak.common.Status.DELETED;
import static com.apps.pochak.common.Status.PUBLIC;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PublishRepository publishRepository;

    @Transactional
    public BaseResponseStatus deleteComment(String postPK, String loginUserHandle, CommentDeleteRequestDto requestDto) throws BaseException {
        try {
            String deleteCommentSK = requestDto.getDeletedCommentSK();
            Comment deleteComment = commentRepository.findCommentByCommentSK(postPK, deleteCommentSK);

            // 지우기 위해서는 자기가 쓴 댓글이 맞는지 확인 필요
            if (!loginUserHandle.equals(deleteComment.getCommentUserHandle())) {
                throw new BaseException(BaseResponseStatus.NOT_YOUR_COMMENT);
            }

            if (requestDto.getDeletedCommentParentSK() == null) {
                deleteChildComments(deleteComment);
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

    public void deleteChildComments(Comment deleteComment) throws BaseException {
        List<Comment> deleteChildCommentList
                = commentRepository.findChildCommentByParentCommentSKAndPostPK(deleteComment.getUploadedDate(), deleteComment.getPostPK());
        commentRepository.deleteComments(deleteChildCommentList);
    }

    @Transactional
    public BaseResponseStatus commentUpload(String postPK,
                                            CommentUploadRequestDto requestDto,
                                            String loginUserHandle) throws BaseException {
        try {
            // comment Entity 생성

            User loginUser = userRepository.findUserByUserHandle(loginUserHandle);
            Post commentedPost = postRepository.findPostByPostPK(postPK);
            String uploadedDate;

            String parentCommentSK = requestDto.getParentCommentSK();

            // parent
            if (parentCommentSK == null) {
                uploadedDate = "COMMENT#" + "PARENT#" + LocalDateTime.now();
                commentedPost.getParentCommentSKs().add(uploadedDate);
                postRepository.savePost(commentedPost);
            } else {
                // child
                uploadedDate = "COMMENT#" + "CHILD#" + LocalDateTime.now();
                Comment parentComment = commentRepository.findCommentByCommentSK(postPK, parentCommentSK);
                parentComment.getChildCommentSKs().add(uploadedDate);
                commentRepository.saveComment(parentComment);
            }

            Comment comment = requestDto.toEntity(commentedPost, loginUserHandle, uploadedDate);
            comment.setStatus(PUBLIC);
            commentRepository.saveComment(comment);

//            // TODO: 리팩토링 필요
//            /*
//            Response - Comment가 업로드된 이후 해당 Post의 Comment들 반환: CommentResDto 사용
//             */
//            // List<ParentCommentDto> 생성
//            List<ParentCommentDto> parentCommentDtoList = commentedPost.getParentCommentSKs().stream().map(
//                    parentCommentSortKey -> {
//                        try {
//                            Comment eachComment = commentRepository.findCommentByCommentSK(postPK, parentCommentSortKey);
//                            User commentOwner = userRepository.findUserByUserHandle(eachComment.getCommentUserHandle());
//
//                            // child comment가 있는 경우 - List<ChildCommentDto> 생성
//                            if (!eachComment.getChildCommentSKs().isEmpty()) {
//                                List<ChildCommentDto> childCommentDtos = eachComment.getChildCommentSKs().stream().map(
//                                        childCommentSK -> {
//                                            try {
//                                                Comment childComment = commentRepository
//                                                        .findCommentByCommentSK(postPK, childCommentSK);
//                                                User childCommentOwner = userRepository
//                                                        .findUserByUserHandle(childComment.getCommentUserHandle());
//                                                return new ChildCommentDto(childCommentOwner, childComment);
//                                            } catch (BaseException e) {
//                                                throw new RuntimeException(e);
//                                            }
//                                        }
//                                ).collect(Collectors.toList());
//                                return new ParentCommentDto(commentOwner, eachComment, childCommentDtos);
//                            }
//                            return new ParentCommentDto(commentOwner, eachComment);
//                        } catch (BaseException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//            ).collect(Collectors.toList());

            return SUCCESS;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            System.err.println(e);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
