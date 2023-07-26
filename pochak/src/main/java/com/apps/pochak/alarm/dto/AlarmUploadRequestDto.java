package com.apps.pochak.alarm.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.AlarmType;
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
public class AlarmUploadRequestDto {
    // test dto
    private String userPK;
    private PostId postId;
    private UserId userId;

    //어차피 test dto라서 실제로 이렇게 안받겠지만.. test 용으로 alarmType String으로 받았습니다
    private String alarmType;

    public Alarm toEntity() {
        if (alarmType.equals("COMMENT")) { // comment만 test함!
            CommentAlarm commentAlarm = new CommentAlarm();
            commentAlarm.setUserPK(userPK);
            commentAlarm.setCommentUserId(userId);
            commentAlarm.setCommentedpostId(postId);
            return commentAlarm;
        }
        return null;
    }
}
