package com.apps.pochak.alarm.dto.response.alarm_element;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.dto.response.AlarmElement;
import com.apps.pochak.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowAlarmElement extends AlarmElement {
    private String profileImage;
    private String userHandle;

    public FollowAlarmElement(Alarm alarm) {
        super(alarm);
        final Member sender = alarm.getFollow().getSender();
        this.profileImage = sender.getProfileImage();
        this.userHandle = sender.getHandle();
    }
}
