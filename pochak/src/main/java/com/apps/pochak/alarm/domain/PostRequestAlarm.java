package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class PostRequestAlarm extends Alarm {

    @DynamoDBAttribute
    private String postPK;

    @DynamoDBAttribute
    private String taggedPostImage;

    @Builder
    public PostRequestAlarm(String receiveUser, String sentUserHandle, String profileImage, String taggedPostPK, String taggedPostImage) {
        super(receiveUser, sentUserHandle, profileImage);
        this.setAlarmType(AlarmType.POST_REQUEST);
        this.postPK = taggedPostPK;
        this.taggedPostImage = taggedPostImage;
    }
}
