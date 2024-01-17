package com.apps.pochak.follow.domain.repository;

import com.apps.pochak.follow.domain.Follow;
import com.apps.pochak.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    long countFollowByReceiver(Member member);

    long countFollowBySender(Member member);

    boolean existsBySenderAndReceiver(Member sender, Member receiver);
}
