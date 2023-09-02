package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentAlarm extends Alarm {

    @DynamoDBAttribute
    private String commentUserHandle; // user who upload comment

    @DynamoDBAttribute
    private String commentedPostPK;

    @DynamoDBAttribute
    private String commentContent;

    public CommentAlarm() {
        this.setAlarmType(AlarmType.COMMENT);
    }
}
