package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeAlarm extends Alarm {

    @DynamoDBAttribute
    private String likeUserHandle; // user who press like btn

    @DynamoDBAttribute
    private String likedPostPK;

    public LikeAlarm() {
        this.setAlarmType(AlarmType.LIKE);
    }
}
