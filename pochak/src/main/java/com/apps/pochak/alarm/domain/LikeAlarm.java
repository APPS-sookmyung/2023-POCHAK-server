package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeAlarm extends Alarm {

    @DynamoDBAttribute
    private String postPK;

    @DynamoDBAttribute
    private String likedPostImage;

    @Builder
    public LikeAlarm(String postPK, String likedPostImage) {
        this.setAlarmType(AlarmType.LIKE);
        this.postPK = postPK;
        this.likedPostImage = likedPostImage;
    }
}
