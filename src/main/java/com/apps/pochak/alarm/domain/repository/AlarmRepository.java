package com.apps.pochak.alarm.domain.repository;

import com.apps.pochak.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Modifying
    @Query("update Alarm alarm set alarm.status = 'DELETED' where alarm.receiver.id = :receiverId")
    void deleteAlarmByMemberId(@Param("receiverId") final Long receiverId);
}