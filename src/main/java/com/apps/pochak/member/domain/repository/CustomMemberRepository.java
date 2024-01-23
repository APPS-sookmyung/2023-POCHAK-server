package com.apps.pochak.member.domain.repository;

import com.apps.pochak.member.domain.Member;
import com.apps.pochak.member.dto.response.MemberElement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomMemberRepository extends JpaRepository<Member, Long> {

    // TODO: 테스트 필요
    @Query("select  " +
            "new com.apps.pochak.member.dto.response.MemberElement(" +
                "m.profileImage, " +
                "m.handle, " +
                "m.name, " +
                "(case when m.id = :loginMemberId then nullif(m.id, :loginMemberId) else (fo.id is not null) end) " +
            ") " +
            "from Follow f " +
            "join Member m on f.sender = m and f.receiver = :member " +
            "left join Follow fo on (fo.receiver = f.sender and fo.sender.id = :loginMemberId and fo.status = 'ACTIVE')")
    Page<MemberElement> findFollowersAndIsFollow(
            @Param("member") final Member member,
            @Param("loginMemberId") final Long loginMemberId,
            final Pageable pageable
    );

    // TODO: 테스트 필요
    @Query("select  " +
            "new com.apps.pochak.member.dto.response.MemberElement(" +
                "m.profileImage, " +
                "m.handle, " +
                "m.name, " +
                "(case when m.id != :loginMemberId then nullif(m.id, :loginMemberId) else (fo.id is not null) end) " +
            ") " +
            "from Follow f " +
            "join Member m on f.receiver = m and f.sender = :member " +
            "left join Follow fo on (fo.receiver = f.receiver and fo.sender.id = :loginMemberId and fo.status = 'ACTIVE')")
    Page<MemberElement> findFollowingsAndIsFollow(
            @Param("member") final Member member,
            @Param("loginMemberId") final Long loginMemberId,
            final Pageable pageable
    );
}
