package com.apps.pochak.comment.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.domain.CommentId;
import com.apps.pochak.common.BaseException;
import lombok.RequiredArgsConstructor;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.domain.CommentId;
import org.springframework.stereotype.Repository;

import static com.apps.pochak.common.BaseResponseStatus.INVALID_COMMENT_ID;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final CommentCrudRepository commentCrudRepository;
    private final DynamoDBMapper mapper;

    public Comment findCommentByCommentId(CommentId commentId) throws BaseException{
        // commentId에 해당하는 Comment 찾기
        return commentCrudRepository.findById(commentId).orElseThrow(()->new BaseException(INVALID_COMMENT_ID));
    }
}