package com.apps.pochak.member.domain.repository;

import com.apps.pochak.global.apiPayload.exception.GeneralException;
import com.apps.pochak.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.INVALID_MEMBER_HANDLE;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByHandle(String handle);

    Optional<Member> findMemberBySocialId(String socialId);

    @Modifying
    @Query("update Member member set member.status = 'DELETED' where member.id = :memberId")
    void deleteMemberByMemberId(@Param("memberId") final Long memberId);

    default Member findByHandle(String handle) {
        return findMemberByHandle(handle).orElseThrow(() -> new GeneralException(INVALID_MEMBER_HANDLE));
    }
}