package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.apps.pochak.alarm.domain.AlarmType.LIKE;

@Getter
@Setter
public class LikeAlarm extends Alarm {

    @DynamoDBAttribute
    private String postPK;

    @DynamoDBAttribute
    private String likedPostImage;

    @Builder
    public LikeAlarm(String receiveUserHandle, User sentUser, Post post) {
        super(receiveUserHandle, sentUser);
        setAlarmType(LIKE);
        this.postPK = post.getPostPK();
        this.likedPostImage = post.getImgUrl();
    }
}
