package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.apps.pochak.post.domain.PostId;
import com.apps.pochak.user.domain.UserId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentAlarm extends Alarm {
    @DynamoDBAttribute
    private PostId commentedpostId;
    @DynamoDBAttribute
    private UserId commentUserId;

    public CommentAlarm() {
        this.setAlarmType(AlarmType.COMMENT);
    }
}
