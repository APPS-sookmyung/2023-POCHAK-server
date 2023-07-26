package com.apps.pochak.alarm.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.AlarmId;
import com.apps.pochak.alarm.domain.AlarmType;
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
    private PostId postId;
    private UserId userId;

    @DynamoDBTypeConvertedEnum
    private AlarmType alarmType;

    private String commentContent;

    public Alarm toEntity() {
        return Alarm.builder()
                .userPK(userPK)
                .alarmUserId(userId)
                .alarmPostId(postId)
                .commentContent(commentContent)
                .alarmType(alarmType)
                .build();
    }
}
