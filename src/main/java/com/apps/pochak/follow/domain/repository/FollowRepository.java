package com.apps.pochak.follow.domain.repository;

import com.apps.pochak.follow.domain.Follow;
import com.apps.pochak.global.apiPayload.exception.GeneralException;
import com.apps.pochak.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.NOT_FOLLOW;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query(value = "select count(f) from Follow f where f.receiver = :member and f.status = 'ACTIVE'")
    long countActiveFollowByReceiver(@Param("member") final Member member);

    @Query(value = "select count(f) from Follow f where f.sender = :member and f.status = 'ACTIVE'")
    long countActiveFollowBySender(@Param("member") final Member member);

    @Query(value = "select count(f.id) > 0 from Follow f " +
            "where f.sender = :sender and f.receiver = :receiver and f.status = 'ACTIVE'")
    boolean existsBySenderAndReceiver(@Param("sender") final Member sender, @Param("receiver") final Member receiver);

    Optional<Follow> findFollowBySenderAndReceiver(final Member sender, final Member receiver);

    default Follow findBySenderAndReceiver(final Member sender, final Member receiver) {
        return findFollowBySenderAndReceiver(sender, receiver).orElseThrow(() -> new GeneralException(NOT_FOLLOW));
    }

    @Modifying
    @Query(value = "update Follow follow " +
            "set follow.status = 'DELETED' " +
            "where follow.receiver.id = :memberId or follow.sender.id = :memberId")
    void deleteFollowByMemberId(@Param("memberId") final Long memberId);
}
