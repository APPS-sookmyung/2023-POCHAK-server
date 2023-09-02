package com.apps.pochak.alarm.controller;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.dto.CommentAlarmRequestDto;
import com.apps.pochak.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    // test api
    @PostMapping("")
    public Alarm testCreateAlarm(@RequestBody CommentAlarmRequestDto requestDto) {
        return alarmService.saveCommentAlarm(requestDto);
    }

}
