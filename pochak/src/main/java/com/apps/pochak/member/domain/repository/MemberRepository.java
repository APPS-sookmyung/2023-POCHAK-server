package com.apps.pochak.member.domain.repository;

import com.apps.pochak.global.apiPayload.exception.GeneralException;
import com.apps.pochak.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.INVALID_MEMBER_HANDLE;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByHandle(String handle);

    Optional<Member> findMemberBySocialId(String socialId);

    default Member findByHandle(String handle) {
        return findMemberByHandle(handle).orElseThrow(() -> new GeneralException(INVALID_MEMBER_HANDLE));
    }
}
