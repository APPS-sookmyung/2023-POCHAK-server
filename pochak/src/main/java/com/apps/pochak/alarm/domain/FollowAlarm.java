package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.apps.pochak.user.domain.UserId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowAlarm extends Alarm {
    @DynamoDBAttribute
    private UserId followUserId;

    public FollowAlarm() {
        this.setAlarmType(AlarmType.FOLLOW);
    }
}
