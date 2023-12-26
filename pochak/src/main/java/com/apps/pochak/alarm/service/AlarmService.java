package com.apps.pochak.alarm.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.PostRequestAlarm;
import com.apps.pochak.alarm.dto.PublicAlarmsResDto;
import com.apps.pochak.alarm.repository.AlarmRepository;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponseStatus;
import com.apps.pochak.common.Status;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.repository.PostRepository;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.common.BaseResponseStatus.*;
import static com.apps.pochak.common.Status.PRIVATE;
import static com.apps.pochak.common.Status.PUBLIC;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final DynamoDBMapper mapper;

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

    public BaseResponseStatus allowPostUpload(String alarmSK, String loginUserHandle) throws BaseException {
        try {
            PostRequestAlarm postRequestAlarm = alarmRepository.findPostRequestAlarmWithUserHandleAndAlarmSK(loginUserHandle, alarmSK);
            postRequestAlarm.setStatus(PRIVATE);
            alarmRepository.saveAlarm(postRequestAlarm);

            Post post = postRepository.findPostByPostPK(postRequestAlarm.getPostPK());
            List<Status>  checkStatusList = post.getTaggedUserHandles().stream().map(
                    taggedUserHandle -> {
                        PostRequestAlarm alarm = null; // TODO Alarm 조회 방법 찾기
                        try {
                            alarm = alarmRepository.findPostRequestAlarmWithUserHandleAndAlarmSK(taggedUserHandle, postRequestAlarm.getSentDate());
                        } catch (BaseException e) {
                            throw new RuntimeException(e);
                        }
                        return alarm.getStatus();
                    }).collect(Collectors.toList());

            if (checkStatusList.contains(PUBLIC))
                return SUCCESS;
            else {
                postRepository.deletePost(post); // TODO SK update 좋은 방법 찾기
                post.setStatus(PUBLIC);
                post.setAllowedDate(LocalDateTime.now());
                postRepository.savePost(post);
            }
            return ALL_ALLOW_POST;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
