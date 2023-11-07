package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestAlarm extends Alarm {

    @DynamoDBAttribute
    private String taggedPostPK;

    @DynamoDBAttribute
    private String taggedPostImage;

    @Builder
    public PostRequestAlarm(String taggedPostPK, String taggedPostImage) {
        this.setAlarmType(AlarmType.POST_REQUEST);
        this.taggedPostPK = taggedPostPK;
        this.taggedPostImage = taggedPostImage;
    }
}
