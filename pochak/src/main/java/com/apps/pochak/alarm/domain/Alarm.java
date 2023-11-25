package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.apps.pochak.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@DynamoDBTable(tableName = "pochakdatabase")
public class Alarm extends BaseEntity {

    @Id
    private AlarmId alarmId;
    private String userHandle; // user who receive the alarm
    private String sentDate;
    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute
    @Getter
    @Setter
    private AlarmType alarmType;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String userSentAlarmHandle;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String userSentAlarmProfileImage;

    @DynamoDBHashKey(attributeName = "PartitionKey")
    public String getUserHandle() {
        return alarmId != null ? alarmId.getUserHandle() : null;
    }

    public void setUserHandle(String userHandle) {
        if (alarmId == null) {
            alarmId = new AlarmId();
        }
        alarmId.setUserHandle(userHandle);
    }

    @DynamoDBRangeKey(attributeName = "SortKey")
    public String getSentDate() {
        return alarmId != null ? alarmId.getSentDate() : null;
    }

    @DynamoDBIgnore
    public LocalDateTime getValidSentDate() {
        return LocalDateTime.parse(alarmId.getSentDate().substring(6));
    }

    /**
     * 사용 유의! 앞에 Prefix(ALARM#) 붙어있어야 함.
     *
     * @param sentDate
     */
    public void setSentDate(String sentDate) {
        if (alarmId == null) {
            alarmId = new AlarmId();
        }
        alarmId.setSentDate(sentDate);
    }

    public void setSentDate(LocalDateTime sentDate) {
        if (alarmId == null) {
            alarmId = new AlarmId();
        }
        alarmId.setSentDate("ALARM#" + sentDate.toString());
    }

    public Alarm(String receiveUser, String sentUserHandle, String profileImage) {
        this.userHandle = receiveUser;
        this.setSentDate("ALARM#");
        this.userSentAlarmHandle = sentUserHandle;
        this.userSentAlarmProfileImage = profileImage;
    }
}
