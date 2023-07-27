package com.apps.pochak.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import java.io.Serializable;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;

@DynamoDBTyped(DynamoDBAttributeType.M)
public class UserId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userPK;
    private String userSK;

    @DynamoDBHashKey
    public String getUserPK() {
        return userPK;
    }

    public void setUserPK(String userPK) {
        this.userPK = userPK;
    }

    @DynamoDBRangeKey
    public String getUserSK() {
        return userSK;
    }

    public void setUserSK(String userSK) {
        this.userSK = userSK;
    }
}
