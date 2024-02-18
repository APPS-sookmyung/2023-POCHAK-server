package com.apps.pochak.like.domain.repository;

import com.apps.pochak.like.domain.LikeEntity;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    int countByLikedPost(final Post post);

    Boolean existsByLikeMemberAndLikedPost(final Member member,
                                           final Post post);

    @Modifying
    @Query(value = "update LikeEntity like " +
            "set like.status = 'DELETED' " +
            "where like.likeMember.id = :memberId or like.likedPost.owner.id = :memberId")
    void deleteLikeByMemberId(@Param("memberId") final Long memberId);
}
