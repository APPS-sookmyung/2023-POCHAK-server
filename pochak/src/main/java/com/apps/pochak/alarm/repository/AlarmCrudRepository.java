package com.apps.pochak.alarm.repository;

import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.alarm.domain.AlarmId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlarmCrudRepository extends DynamoDBCrudRepository<Alarm, AlarmId> {
    Optional<Alarm> findAlarmByUserHandleAndSentDateStartingWith(String userHandle, String prefix);
}
