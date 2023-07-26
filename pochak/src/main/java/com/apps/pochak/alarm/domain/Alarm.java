package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.apps.pochak.annotation.CustomGeneratedKey;
import com.apps.pochak.common.BaseEntity;
import com.apps.pochak.post.domain.PostId;
import com.apps.pochak.user.domain.UserId;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.*;

@NoArgsConstructor
@DynamoDBTable(tableName = "pochakdb")
public class Alarm extends BaseEntity {

    @Id
    private AlarmId alarmId;
    private String userPK; // user who receive the alarm
    private String alarmSK;
    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute
    @Getter
    @Setter
    private AlarmType alarmType;

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.M)
    private UserId alarmUserId; // user who sent the alarm

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.M)
    private PostId alarmPostId;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String commentContent;  // if type is comment

    @DynamoDBHashKey(attributeName = "Partition key")
    public String getUserPK() {
        return alarmId != null ? alarmId.getUserPK() : null;
    }

    public void setUserPK(String userPK) {
        if (alarmId == null) {
            alarmId = new AlarmId();
        }
        alarmId.setUserPK(userPK);
    }

    @DynamoDBRangeKey(attributeName = "Sort Key")
    @CustomGeneratedKey(prefix = "ALARM#")
    public String getAlarmSK() {
        return alarmId != null ? alarmId.getAlarmSK() : null;
    }

    public void setAlarmSK(String alarmSK) {
        if (alarmId == null) {
            alarmId = new AlarmId();
        }
        alarmId.setAlarmSK(alarmSK);
    }

    @Builder
    public Alarm(String userPK, AlarmType alarmType, UserId alarmUserId, PostId alarmPostId, String commentContent) {
        this.alarmId = new AlarmId();
        alarmId.setUserPK(userPK);

        this.userPK = userPK;
        this.alarmType = alarmType;
        this.alarmUserId = alarmUserId;
        this.alarmPostId = alarmPostId;
        this.commentContent = commentContent;
    }
}
