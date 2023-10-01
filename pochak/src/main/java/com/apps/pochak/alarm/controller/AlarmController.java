package com.apps.pochak.alarm.controller;

import com.apps.pochak.alarm.dto.PublicAlarmsResDto;
import com.apps.pochak.alarm.service.AlarmService;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.login.jwt.JwtHeaderUtil;
import com.apps.pochak.login.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
    private final AlarmService alarmService;
    private final JwtService jwtService;

    @GetMapping("")
    public BaseResponse<PublicAlarmsResDto> getAllAlarms() {
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            return new BaseResponse<>(alarmService.getAllPublicAlarms(loginUserHandle));
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }
}
