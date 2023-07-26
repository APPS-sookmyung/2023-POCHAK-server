package com.apps.pochak.alarm.repository;

import com.apps.pochak.alarm.domain.AlarmId;
import com.apps.pochak.alarm.domain.CommentAlarm;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface CommentAlarmRepository extends CrudRepository<CommentAlarm, AlarmId> {
}
