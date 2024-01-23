package com.apps.pochak.tag.domain.repository;

import com.apps.pochak.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Modifying
    @Query("""
            update Tag tag
            set tag.status = 'DELETED'
            where tag.member.id = :memberId or tag.post.owner.id = :memberId
            """)
    void deleteTagByMemberId(@Param("memberId") final Long memberId);
}
