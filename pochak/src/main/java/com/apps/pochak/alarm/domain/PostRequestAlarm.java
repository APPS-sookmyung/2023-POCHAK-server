package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestAlarm extends Alarm {

    @DynamoDBAttribute
    private String postOwnerHandle; // user who sent the alarm

    @DynamoDBAttribute
    private String taggedPostPK;

    public PostRequestAlarm() {
        this.setAlarmType(AlarmType.POST_REQUEST);
    }
}
