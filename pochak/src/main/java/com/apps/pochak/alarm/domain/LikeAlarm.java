package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeAlarm extends Alarm {

    @DynamoDBAttribute
    private String postPK;

    @DynamoDBAttribute
    private String likedPostImage;

    @Builder
    public LikeAlarm(User receiveUser, User sentUserHandle, Post post) {
        super(receiveUser.getHandle(), sentUserHandle, sentUserHandle.getCreatedDate().toString());
        setAlarmType(AlarmType.LIKE);
        setPostPK(post.getPostPK());
        setLikedPostImage(post.getImgUrl());
    }
}
