package com.apps.pochak.comment.service;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.dto.CommentDeleteRequestDto;
import com.apps.pochak.comment.repository.CommentRepository;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.apps.pochak.common.BaseResponseStatus.DATABASE_ERROR;
import static com.apps.pochak.common.BaseResponseStatus.NOT_YOUR_COMMENT;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public BaseResponse deleteComment(String postPK, String loginUserHandle, CommentDeleteRequestDto requestDto) throws BaseException {
        try{
            String deleteCommentSK= requestDto.getCommentSK();
            Comment deleteComment=commentRepository.findCommentByCommentSK(postPK,deleteCommentSK);
            if(!loginUserHandle.equals(deleteComment.getCommentUserHandle())){
                // 지우기 위해서는 자기가 쓴 댓글이 맞는지 확인 필요
                throw new BaseException(NOT_YOUR_COMMENT);
            }

            if(requestDto.getParentCommentSK()==null){
                // delete parent comment
            }else {
                // delete child comment
                // parentComment 의 child sk List에서 child comment 제거
                String parentCommentSK= requestDto.getParentCommentSK();
                Comment parentComment=commentRepository.findCommentByCommentSK(postPK,parentCommentSK);
                parentComment.getChildCommentSKs().remove(deleteCommentSK);
                commentRepository.saveComment(parentComment);
            }

            // 삭제
            return commentRepository.deleteComment(deleteComment);

        }catch (BaseException e){
            throw e;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
