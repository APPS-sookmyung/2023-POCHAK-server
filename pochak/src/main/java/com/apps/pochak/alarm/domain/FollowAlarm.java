package com.apps.pochak.alarm.domain;

import com.apps.pochak.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.apps.pochak.alarm.domain.AlarmType.FOLLOW;

@Getter
@Setter
public class FollowAlarm extends Alarm {
    @Builder
    public FollowAlarm(User receiveUser, User sentUser) {
        super(receiveUser.getHandle(), sentUser);
        setAlarmType(FOLLOW);
    }
}
