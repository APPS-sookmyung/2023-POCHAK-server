package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import static com.apps.pochak.alarm.domain.AlarmType.POST_REQUEST;

@Getter
@Setter
public class PostRequestAlarm extends Alarm {

    @DynamoDBAttribute
    private String postPK;

    @DynamoDBAttribute
    private String taggedPostImage;

    public PostRequestAlarm(String receiveUserHandle, User sentUser, Post post) {
        super(receiveUserHandle, sentUser);
        setAlarmType(POST_REQUEST);
        setPostPK(post.getPostPK());
        setTaggedPostImage(post.getImgUrl());
    }
}
