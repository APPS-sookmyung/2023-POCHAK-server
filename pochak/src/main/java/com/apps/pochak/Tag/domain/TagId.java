package com.apps.pochak.Tag.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import java.io.Serializable;
import java.time.LocalDateTime;

@DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.M)
public class TagId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userHandle;
    private String allowedDate;

    @DynamoDBHashKey
    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    @DynamoDBRangeKey
    public String getAllowedDate() {
        return allowedDate;
    }

    public void setAllowedDate(String allowedDate) {
        this.allowedDate = allowedDate;
    }
}
