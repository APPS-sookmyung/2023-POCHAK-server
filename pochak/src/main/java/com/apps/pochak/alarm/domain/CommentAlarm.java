package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.apps.pochak.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.apps.pochak.alarm.domain.AlarmType.COMMENT;

@Getter
@Setter
public class CommentAlarm extends Alarm {

    @DynamoDBAttribute
    private String postPK;

    @DynamoDBAttribute
    private String commentContent;

    @DynamoDBAttribute
    private String commentedPostImage;

    @Builder
    public CommentAlarm(String receiveUserHandle, User sentUser, String postPK, String commentContent, String commentedPostImage) {
        super(receiveUserHandle, sentUser);
        this.setAlarmType(COMMENT);
        this.postPK = postPK;
        this.commentContent = commentContent;
        this.commentedPostImage = commentedPostImage;
    }
}
