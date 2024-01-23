package com.apps.pochak.likes.domain.repository;

import com.apps.pochak.likes.domain.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    @Modifying
    @Query("""
            update LikeEntity like 
            set like.status = 'DELETED' 
            where like.likeMember.id = :memberId or like.likedPost.owner.id = :memberId
            """)
    void deleteLikeByMemberId(@Param("memberId") final Long memberId);
}
