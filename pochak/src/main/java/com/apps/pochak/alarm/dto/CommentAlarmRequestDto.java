package com.apps.pochak.alarm.dto;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.CommentAlarm;
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
    private String userHandle;
    private String postPK;
    private String commentUserHandle;
    private String commentContent;

    public Alarm toEntity() {
        CommentAlarm commentAlarm = new CommentAlarm();
        commentAlarm.setUserHandle(userHandle);
        commentAlarm.setCommentedPostPK(postPK);
        commentAlarm.setCommentUserHandle(commentUserHandle);
        commentAlarm.setCommentContent(commentContent);
        return commentAlarm;
    }
}
