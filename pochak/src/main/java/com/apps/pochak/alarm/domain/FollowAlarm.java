package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowAlarm extends Alarm {

    @DynamoDBAttribute
    private String followingUserHandle; // user who follow

    public FollowAlarm() {
        this.setAlarmType(AlarmType.FOLLOW);
    }
}
