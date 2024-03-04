package com.apps.pochak.alarm.dto.response.alarm_element;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.dto.response.AlarmElement;
import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentAlarmElement extends AlarmElement {
    private Long commentId;
    private String commentContent;

    private Long postId;
    private String postImage;

    private String memberHandle;
    private String memberName;
    private String memberProfileImage;

    public CommentAlarmElement(Alarm alarm) {
        super(alarm);
        final Comment comment = alarm.getComment();
        this.commentId = comment.getId();
        this.commentContent = comment.getContent();

        final Post post = comment.getPost();
        this.postId = post.getId();
        this.postImage = post.getPostImage();

        final Member member = comment.getMember();
        this.memberHandle = member.getHandle();
        this.memberName = member.getName();
        this.memberProfileImage = member.getProfileImage();
    }
}
