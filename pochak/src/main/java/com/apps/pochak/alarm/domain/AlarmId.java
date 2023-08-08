package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import java.io.Serializable;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;

@DynamoDBTyped(DynamoDBAttributeType.M)
public class AlarmId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userHandle;

    private String alarmSK;

    @DynamoDBHashKey
    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    @DynamoDBRangeKey
    public String getAlarmSK() {
        return alarmSK;
    }

    public void setAlarmSK(String alarmSK) {
        this.alarmSK = alarmSK;
    }
}
