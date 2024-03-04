package com.apps.pochak.alarm.domain.repository;

import com.apps.pochak.alarm.domain.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Modifying
    @Query("update Alarm alarm set alarm.status = 'DELETED' where alarm.receiver.id = :receiverId")
    void deleteAlarmByMemberId(@Param("receiverId") final Long receiverId);

    // TODO: performance must be checked
    @Query("select a from Alarm a " +
            "join fetch a.comment c " +
                "join fetch c.post " +
                "join fetch c.member " +
            "join fetch a.follow f " +
                "join fetch f.sender " +
            "join fetch a.like l " +
                "join fetch l.likedPost " +
                "join fetch l.likeMember " +
            "join fetch a.tag t " +
                "join fetch t.post p " +
                "   join fetch p.owner " +
            "where a.receiver.id = :receiverId " +
            "order by a.createdDate desc ")
    Page<Alarm> getAllAlarm(
            @Param("receiverId") final Long receiverId,
            final Pageable pageable
    );
}
