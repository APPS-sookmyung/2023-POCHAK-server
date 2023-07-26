package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.apps.pochak.post.domain.PostId;
import com.apps.pochak.user.domain.UserId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeAlarm extends Alarm {
    @DynamoDBAttribute
    private PostId likedPostId;
    @DynamoDBAttribute
    private UserId likeUserId;

    public LikeAlarm() {
        this.setAlarmType(AlarmType.LIKE);
    }
}
