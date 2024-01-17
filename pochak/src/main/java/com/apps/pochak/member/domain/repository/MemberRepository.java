package com.apps.pochak.member.domain.repository;

import com.apps.pochak.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByHandle(String handle);

    Optional<Member> findMemberBySocialId(String socialId);
}
