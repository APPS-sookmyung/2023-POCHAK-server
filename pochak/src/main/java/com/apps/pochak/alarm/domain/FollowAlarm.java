package com.apps.pochak.alarm.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class FollowAlarm extends Alarm {
    @Builder
    public FollowAlarm(String receiveUser, String sentUserHandle, String profileImage) {
        super(receiveUser, sentUserHandle, profileImage);
        this.setAlarmType(AlarmType.FOLLOW);
    }
}
