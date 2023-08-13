package com.apps.pochak.publish.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import java.io.Serializable;

@DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.M)
public class PublishId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userHandle;
    private String uploadedDate;

    @DynamoDBHashKey
    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    @DynamoDBRangeKey
    public String getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(String uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
}
