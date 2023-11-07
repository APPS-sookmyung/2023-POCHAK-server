package com.apps.pochak.alarm.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowAlarm extends Alarm {
    @Builder
    public FollowAlarm() {
        this.setAlarmType(AlarmType.FOLLOW);
    }
}
