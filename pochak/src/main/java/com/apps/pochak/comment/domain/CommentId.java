package com.apps.pochak.comment.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import java.io.Serializable;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.*;

@DynamoDBTyped(DynamoDBAttributeType.M)
public class CommentId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String postPK;
    private String commentSK;

    @DynamoDBHashKey
    public String getPostPK() {
        return postPK;
    }

    public void setPostPK(String postPK) {
        this.postPK = postPK;
    }

    @DynamoDBRangeKey
    public String getCommentSK() {
        return commentSK;
    }

    public void setCommentSK(String commentSK) {
        this.commentSK = commentSK;
    }
}
