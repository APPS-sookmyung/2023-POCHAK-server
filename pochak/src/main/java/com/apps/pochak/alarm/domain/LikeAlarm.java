package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeAlarm extends Alarm {

    @DynamoDBAttribute
    private String likedPostPK;

    @DynamoDBAttribute
    private String likedPostImage;

    @Builder
    public LikeAlarm(String likedPostPK, String likedPostImage) {
        this.setAlarmType(AlarmType.LIKE);
        this.likedPostPK = likedPostPK;
        this.likedPostImage = likedPostImage;
    }
}
