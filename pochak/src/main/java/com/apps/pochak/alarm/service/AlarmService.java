package com.apps.pochak.alarm.service;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.CommentAlarm;
import com.apps.pochak.alarm.dto.AlarmUploadRequestDto;
import com.apps.pochak.alarm.repository.AlarmRepository;
import com.apps.pochak.alarm.repository.CommentAlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository repository;
    private final CommentAlarmRepository commentAlarmRepository;

    public Alarm save(AlarmUploadRequestDto requestDto) {
        return repository.save(requestDto.toEntity());
    }
}
