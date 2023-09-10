package com.apps.pochak.comment.service;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.dto.ChildCommentDto;
import com.apps.pochak.comment.dto.CommentResDto;
import com.apps.pochak.comment.dto.CommentUploadRequestDto;
import com.apps.pochak.comment.dto.ParentCommentDto;
import com.apps.pochak.comment.repository.CommentRepository;
import com.apps.pochak.common.BaseException;
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
import java.util.stream.Collectors;

import static com.apps.pochak.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PublishRepository publishRepository;

    @Transactional
    public CommentResDto commentUpload(String postPK,
                                       CommentUploadRequestDto requestDto,
                                       String loginUserHandle,
                                       String parentCommentSK) throws BaseException {
        try {
            // comment Entity 생성

            User loginUser = userRepository.findUserByUserHandle(loginUserHandle);
            Post commentedPost = postRepository.findPostByPostPK(postPK);
            String uploadedDate = "COMMENT#" + LocalDateTime.now();
            Comment comment = requestDto.toEntity(commentedPost, loginUserHandle, uploadedDate);
            commentRepository.saveComment(comment);

            /*
            parent comment인 경우
            - post의 parentCommentSKs 리스트 업데이트
             */
            if (parentCommentSK == null) {
                commentedPost.getParentCommentSKs().add(comment.getUploadedDate());
                postRepository.savePost(commentedPost);
            } else {
            /*
            child comment인 경우
            - parent comment의 childCommentSKs 리스트 업데이트
             */
                Comment parentComment = commentRepository.findCommentByCommentSK(postPK, parentCommentSK);
                parentComment.getChildCommentSKs().add(comment.getUploadedDate());
                commentRepository.saveComment(parentComment);
            }

            /*
            Response - Comment가 업로드된 이후 해당 Post의 Comment들 반환: CommentResDto 사용
             */
            // List<ParentCommentDto> 생성
            List<ParentCommentDto> parentCommentDtoList = commentedPost.getParentCommentSKs().stream().map(
                    parentCommentSortKey -> {
                        try {
                            Comment eachComment = commentRepository.findCommentByCommentSK(postPK, parentCommentSortKey);
                            User commentOwner = userRepository.findUserByUserHandle(eachComment.getCommentUserHandle());

                            // child comment가 있는 경우 - List<ChildCommentDto> 생성
                            if (!eachComment.getChildCommentSKs().isEmpty()) {
                                List<ChildCommentDto> childCommentDtos = eachComment.getChildCommentSKs().stream().map(
                                        childCommentSK -> {
                                            try {
                                                Comment childComment = commentRepository
                                                        .findCommentByCommentSK(postPK, childCommentSK);
                                                User childCommentOwner = userRepository
                                                        .findUserByUserHandle(childComment.getCommentUserHandle());
                                                return new ChildCommentDto(childCommentOwner, childComment);
                                            } catch (BaseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                ).collect(Collectors.toList());
                                return new ParentCommentDto(commentOwner, eachComment, childCommentDtos);
                            }
                            return new ParentCommentDto(commentOwner, eachComment);
                        } catch (BaseException e) {
                            throw new RuntimeException(e);
                        }
                    }
            ).collect(Collectors.toList());

            // TODO: 이후 Limit 추가 필요
            return new CommentResDto(parentCommentDtoList);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            System.err.println(e);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
