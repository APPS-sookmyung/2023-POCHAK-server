package com.apps.pochak.alarm.dto.response;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.AlarmType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmElement {
    private AlarmType alarmType;
    private Boolean isChecked;

    protected AlarmElement (Alarm alarm) {
        this.alarmType = alarm.getAlarmType();
        this.isChecked = alarm.getIsChecked();
    }
}
