package com.apps.pochak.alarm.service;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.dto.PublicAlarmsResDto;
import com.apps.pochak.alarm.repository.AlarmRepository;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponseStatus;
import com.apps.pochak.post.repository.PostRepository;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.apps.pochak.common.BaseResponseStatus.DATABASE_ERROR;
import static com.apps.pochak.common.BaseResponseStatus.SUCCESS;
import static com.apps.pochak.common.Status.PRIVATE;

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

    public BaseResponseStatus makeAlarmPrivate(String alarmSK, String loginUserHandle) throws BaseException {
        try {
            Alarm alarm = alarmRepository.findAlarmWithAlarmSK(loginUserHandle, alarmSK);
            alarm.setStatus(PRIVATE);
            alarmRepository.saveAlarm(alarm);

            return SUCCESS;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // TODO post 게시 수락
    public BaseResponseStatus allowPostUpload(String alarmSK, String loginUserHandle) throws BaseException {
        try {
            // 알람 조회 후 userhandle 확인해서 삭제 진행
            Alarm alarm = alarmRepository.findAlarmWithAlarmSK(loginUserHandle, alarmSK);
            alarmRepository.deleteAlarm(alarm);
            return SUCCESS;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
