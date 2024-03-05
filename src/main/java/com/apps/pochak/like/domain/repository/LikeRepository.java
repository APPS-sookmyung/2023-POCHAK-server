package com.apps.pochak.like.domain.repository;

import com.apps.pochak.like.domain.LikeEntity;
import com.apps.pochak.like.dto.response.LikeElement;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    @Query("select count(l) from LikeEntity l where l.likedPost = :post and l.status = 'ACTIVE'")
    int countByLikedPost(@Param("post") final Post post);

    @Query("select count(l) > 0 from LikeEntity l " +
            "where l.likeMember = :member " +
            "   and l.likedPost = :post " +
            "   and l.status = 'ACTIVE'")
    Boolean existsByLikeMemberAndLikedPost(
            @Param("member") final Member member,
            @Param("post") final Post post
    );

    Optional<LikeEntity> findByLikeMemberAndLikedPost(
            final Member member,
            final Post post
    );

    @Modifying
    @Query(value = "update LikeEntity like " +
            "set like.status = 'DELETED' " +
            "where like.likeMember.id = :memberId or like.likedPost.owner.id = :memberId")
    void deleteLikeByMemberId(@Param("memberId") final Long memberId);

    @Query(value = "select l from LikeEntity l where l.lastModifiedDate > :nowMinusOneHour ")
    List<LikeEntity> findModifiedLikeEntityWithinOneHour(@Param("nowMinusOneHour") final LocalDateTime nowMinusOneHour);

    @Query("select  " +
            "new com.apps.pochak.like.dto.response.LikeElement(" +
            "m.handle, " +
            "m.profileImage, " +
            "m.name, " +
            "(case when m.id <> :loginMemberId then (f.sender is not null) else nullif(m.id, :loginMemberId) end) " +
            ") " +
            "from LikeEntity l " +
            "left join Member m on l.likeMember = m and m.status = 'ACTIVE' " +
            "left join Follow f on (f.sender.id = :loginMemberId and f.receiver = l.likeMember) and f.status <> 'DELETED' " +
            "where l.status = 'ACTIVE' and l.likedPost = :post " +
            "order by f.lastModifiedDate desc ")
    List<LikeElement> findFollowersAndIsFollow(
            @Param("loginMemberId") final Long loginMemberId,
            @Param("post") final Post post
    );
}
