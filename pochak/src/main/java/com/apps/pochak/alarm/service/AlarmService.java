package com.apps.pochak.alarm.service;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.dto.PublicAlarmsResDto;
import com.apps.pochak.alarm.repository.AlarmRepository;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.post.repository.PostRepository;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apps.pochak.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PublicAlarmsResDto getAllPublicAlarms(String loginUserHandle) throws BaseException {
        try {
            List<Alarm> publicAlarmList
                    = alarmRepository.findAllPublicAlarmsWithUserHandle(loginUserHandle);

            return new PublicAlarmsResDto(publicAlarmList);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
