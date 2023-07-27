package com.apps.pochak.alarm.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.apps.pochak.annotation.CustomGeneratedKey;
import com.apps.pochak.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@DynamoDBTable(tableName = "pochakdatabase")
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

    @DynamoDBHashKey(attributeName = "PartitionKey")
    public String getUserPK() {
        return alarmId != null ? alarmId.getUserPK() : null;
    }

    public void setUserPK(String userPK) {
        if (alarmId == null) {
            alarmId = new AlarmId();
        }
        alarmId.setUserPK(userPK);
    }

    @DynamoDBRangeKey(attributeName = "SortKey")
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
}
