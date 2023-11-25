package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
public class LikeAlarm extends Alarm {

    @DynamoDBAttribute
    private String postPK;

    @DynamoDBAttribute
    private String likedPostImage;

    @Builder
    public LikeAlarm(String receiveUser, String sentUserHandle, String profileImage, String likedPostPK, String likedPostImage) {
        super(receiveUser, sentUserHandle, profileImage);
        this.setAlarmType(AlarmType.LIKE);
        this.postPK = likedPostPK;
        this.likedPostImage = likedPostImage;
    }
}
