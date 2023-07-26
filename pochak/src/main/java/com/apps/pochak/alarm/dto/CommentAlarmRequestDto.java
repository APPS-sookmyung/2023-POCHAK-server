package com.apps.pochak.alarm.dto;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.CommentAlarm;
import com.apps.pochak.post.domain.PostId;
import com.apps.pochak.user.domain.UserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentAlarmRequestDto {
    // test dto
    private String userPK;
    private PostId commentedPostId;
    private UserId commentUserId;
    private String commentContent;

    public Alarm toEntity() {
        CommentAlarm commentAlarm = new CommentAlarm();
        commentAlarm.setUserPK(userPK);
        commentAlarm.setCommentUserId(commentUserId);
        commentAlarm.setCommentedPostId(commentedPostId);
        commentAlarm.setCommentContent(commentContent);
        return commentAlarm;
    }
}
