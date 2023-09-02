package com.apps.pochak.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import java.io.Serializable;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;

@DynamoDBTyped(DynamoDBAttributeType.M)
public class UserId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String handle;
    private String userSK = "USER#";

    @DynamoDBHashKey
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    @DynamoDBRangeKey
    public String getUserSK() {
        return userSK;
    }

    public void setUserSK(String userSK) {
        this.userSK = userSK;
    }
}
