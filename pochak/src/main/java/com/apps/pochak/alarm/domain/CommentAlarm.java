package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.apps.pochak.post.domain.PostId;
import com.apps.pochak.user.domain.UserId;
import lombok.Getter;
import lombok.Setter;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;

@Getter
@Setter
public class CommentAlarm extends Alarm {

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.M)
    private UserId commentUserId; // user who upload comment

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.M)
    private PostId commentedPostId;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String commentContent;

    public CommentAlarm() {
        this.setAlarmType(AlarmType.COMMENT);
    }
}
