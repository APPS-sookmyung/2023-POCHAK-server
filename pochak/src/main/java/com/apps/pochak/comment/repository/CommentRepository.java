package com.apps.pochak.comment.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.domain.CommentId;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.apps.pochak.common.BaseResponseStatus.*;
import static com.apps.pochak.common.BaseResponseStatus.INVALID_COMMENT_ID;
import static com.apps.pochak.common.BaseResponseStatus.NULL_COMMENTS;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final CommentCrudRepository commentCrudRepository;
    private final DynamoDBMapper mapper;

    public Comment findCommentByCommentId(CommentId commentId) throws BaseException {
        // commentId에 해당하는 Comment 찾기
        return commentCrudRepository.findById(commentId).orElseThrow(() -> new BaseException(INVALID_COMMENT_ID));
    }

    public Comment findRandomCommentsByPostPK(String postPK) throws BaseException {
        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(postPK));
        eav.put(":val2", new AttributeValue().withS("COMMENT#"));

        DynamoDBQueryExpression<Comment> query = new DynamoDBQueryExpression<Comment>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);

        List<Comment> comments = mapper.query(Comment.class, query);

        if (comments.isEmpty()) {
            return null;
        }
        return comments.get((int) (Math.random() * comments.size()));
    }

    public Comment findCommentByCommentSK(String postPK, String commentSK) throws BaseException {
        /*
        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(postPK));
        eav.put(":val2", new AttributeValue().withS(commentSK));

        DynamoDBQueryExpression<Comment> query = new DynamoDBQueryExpression<Comment>()
                .withKeyConditionExpression("#PK = :val1 and #SK = :val2")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);

        List<Comment> comments = mapper.query(Comment.class, query);

        if (comments.isEmpty()) {
            throw new BaseException(INVALID_COMMENT_ID);
        }
        return comments.get(0);
         */
        return commentCrudRepository.findCommentByPostPKAndUploadedDateStartingWith(postPK, commentSK)
                .orElseThrow(() -> new BaseException(INVALID_COMMENT_SK));
    }

    public Comment saveComment(Comment comment){
        return commentCrudRepository.save(comment);
    }

}