package com.apps.pochak.follow.domain.repository;

import com.apps.pochak.follow.domain.Follow;
import com.apps.pochak.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query(value = "select f from Follow f where f.receiver = :member and f.status = 'ACTIVE'")
    long countActiveFollowByReceiver(@Param("member") Member member);

    @Query(value = "select f from Follow f where f.sender = :member and f.status = 'ACTIVE'")
    long countActiveFollowBySender(@Param("member") Member member);

    @Query(value = "select f from Follow f " +
            "where f.sender = :sender and f.receiver = :receiver and f.status = 'ACTIVE'")
    boolean existsBySenderAndReceiver(@Param("sender") Member sender, @Param("receiver") Member receiver);

    Optional<Follow> findFollowBySenderAndReceiver(Member sender, Member receiver);
}
