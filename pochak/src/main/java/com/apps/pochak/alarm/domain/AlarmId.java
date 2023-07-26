package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

import java.io.Serializable;

public class AlarmId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userPK;

    private String alarmSK;

    @DynamoDBHashKey
    public String getUserPK() {
        return userPK;
    }

    public void setUserPK(String userPK) {
        this.userPK = userPK;
    }

    @DynamoDBRangeKey
    public String getAlarmSK() {
        return alarmSK;
    }

    public void setAlarmSK(String alarmSK) {
        this.alarmSK = alarmSK;
    }
}
