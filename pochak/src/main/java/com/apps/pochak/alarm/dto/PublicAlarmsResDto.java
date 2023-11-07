package com.apps.pochak.alarm.dto;

import com.apps.pochak.alarm.domain.Alarm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicAlarmsResDto {
    private List<Alarm> alarmList = new ArrayList<>();
}
