package com.apps.pochak.alarm.dto.response.alarm_element;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.dto.response.AlarmElement;
import com.apps.pochak.like.domain.LikeEntity;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeAlarmElement extends AlarmElement {
    private String memberHandle;
    private String memberName;
    private String memberProfileImage;

    private Long postId;
    private String postImage;

    public LikeAlarmElement(Alarm alarm) {
        super(alarm);
        final LikeEntity like = alarm.getLike();

        final Member member = like.getLikeMember();
        this.memberHandle = member.getHandle();
        this.memberName = member.getName();
        this.memberProfileImage = member.getProfileImage();

        final Post post = like.getLikedPost();
        this.postId = post.getId();
        this.postImage = post.getPostImage();
    }
}
