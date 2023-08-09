package com.apps.pochak.post.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;

@DynamoDBTyped(DynamoDBAttributeType.M)
public class PostId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String postPK;
    private LocalDateTime allowedDate;

    @DynamoDBHashKey
    public String getPostPK() {
        return postPK;
    }

    public void setPostPK(String postPK) {
        this.postPK = postPK;
    }

    @DynamoDBRangeKey
    public LocalDateTime getAllowedDate() {
        return allowedDate;
    }

    public void setAllowedDate(LocalDateTime date) {
        this.allowedDate = date;
    }
}
