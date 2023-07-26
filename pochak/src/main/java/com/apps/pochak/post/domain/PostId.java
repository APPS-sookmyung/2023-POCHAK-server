package com.apps.pochak.post.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

import java.io.Serializable;

public class PostId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String postPK;
    private String postSK;

    @DynamoDBHashKey
    public String getPostPK() {
        return postPK;
    }

    public void setPostPK(String postPK) {
        this.postPK = postPK;
    }

    @DynamoDBRangeKey
    public String getPostSK() {
        return postSK;
    }

    public void setPostSK(String postSK) {
        this.postSK = postSK;
    }
}
