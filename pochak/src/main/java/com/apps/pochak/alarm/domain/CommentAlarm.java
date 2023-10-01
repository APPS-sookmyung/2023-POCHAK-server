package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.apps.pochak.alarm.domain.AlarmType.*;

@Getter
@Setter
public class CommentAlarm extends Alarm {

    @DynamoDBAttribute
    private String commentedPostPK;

    @DynamoDBAttribute
    private String commentContent;

    @DynamoDBAttribute
    private String commentedPostImage;

    @Builder
    public CommentAlarm(String commentedPostPK, String commentContent, String commentedPostImage) {
        this.setAlarmType(COMMENT);
        this.commentedPostPK = commentedPostPK;
        this.commentContent = commentContent;
        this.commentedPostImage = commentedPostImage;
    }
}
