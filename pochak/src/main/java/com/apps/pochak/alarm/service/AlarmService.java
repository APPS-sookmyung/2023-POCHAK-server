package com.apps.pochak.alarm.service;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.dto.CommentAlarmRequestDto;
import com.apps.pochak.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository repository;

    // test api - 나중에 원하는 대로 고치세요!!
    public Alarm saveCommentAlarm(CommentAlarmRequestDto requestDto) {
        Alarm alarm = requestDto.toEntity();
        return repository.save(alarm);
    }
}
