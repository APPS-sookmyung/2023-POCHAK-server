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
public class LikeAlarm extends Alarm {

    @DynamoDBAttribute
    @DynamoDBTyped(DynamoDBAttributeType.M)
    private UserId likeUserId; // user who press like btn

    @DynamoDBAttribute
    @DynamoDBTyped(DynamoDBAttributeType.M)
    private PostId likedPostId;

    public LikeAlarm() {
        this.setAlarmType(AlarmType.LIKE);
    }
}
