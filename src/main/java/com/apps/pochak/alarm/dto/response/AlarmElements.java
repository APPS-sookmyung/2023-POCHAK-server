package com.apps.pochak.alarm.dto.response;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.AlarmType;
import com.apps.pochak.alarm.dto.response.alarm_element.CommentAlarmElement;
import com.apps.pochak.alarm.dto.response.alarm_element.FollowAlarmElement;
import com.apps.pochak.alarm.dto.response.alarm_element.LikeAlarmElement;
import com.apps.pochak.alarm.dto.response.alarm_element.TagApprovalAlarmElement;
import com.apps.pochak.global.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmElements {
    private PageInfo pageInfo;
    private List<AlarmElement> alarmElementList;

    public AlarmElements(Page<Alarm> alarmPage) {
        this.pageInfo = new PageInfo(alarmPage);
        this.alarmElementList = alarmPage.stream().map(
                alarm -> {
                    final AlarmType alarmType = alarm.getAlarmType();

                    if (alarmType.equals(AlarmType.FOLLOW)) {
                        return new FollowAlarmElement(alarm);
                    } else if (alarmType.equals(AlarmType.COMMENT)) {
                        return new CommentAlarmElement(alarm);
                    } else if (alarmType.equals(AlarmType.TAG_APPROVAL)) {
                        return new TagApprovalAlarmElement(alarm);
                    } else {
                        return new LikeAlarmElement(alarm);
                    }
                }
        ).collect(Collectors.toList());
    }
}
