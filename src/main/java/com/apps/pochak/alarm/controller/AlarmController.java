package com.apps.pochak.alarm.controller;

import com.apps.pochak.alarm.dto.response.AlarmElements;
import com.apps.pochak.alarm.service.AlarmService;
import com.apps.pochak.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/alarms")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping("")
    public ApiResponse<AlarmElements> getAllAlarms(
            @PageableDefault(30) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(alarmService.getAllAlarms(pageable));
    }
}
