package com.apps.pochak.alarm.dto.response.alarm_element;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.dto.response.AlarmElement;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.tag.domain.Tag;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagApprovalAlarmElement extends AlarmElement {
    private Long tagId;

    private String ownerHandle;
    private String ownerName;
    private String ownerProfileImage;

    private Long postId;
    private String postImage;

    public TagApprovalAlarmElement(Alarm alarm) {
        super(alarm);
        final Tag tag = alarm.getTag();
        this.tagId = tag.getId();

        final Post post = tag.getPost();
        this.postId = post.getId();
        this.postImage = post.getPostImage();

        final Member owner = post.getOwner();
        this.ownerHandle = owner.getHandle();
        this.ownerName = owner.getName();
        this.ownerProfileImage = owner.getProfileImage();
    }
}
