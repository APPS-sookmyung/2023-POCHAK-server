package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.apps.pochak.user.domain.UserId;
import lombok.Getter;
import lombok.Setter;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;

@Getter
@Setter
public class FollowAlarm extends Alarm {

    @DynamoDBAttribute
    @DynamoDBTyped(DynamoDBAttributeType.M)
    private UserId followingUserId; // user who follow

    public FollowAlarm() {
        this.setAlarmType(AlarmType.FOLLOW);
    }
}
