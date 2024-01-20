package com.apps.pochak.like.domain.repository;

import com.apps.pochak.like.domain.LikeEntity;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    int countByLikedPost(final Post post);

    Boolean existsByLikeMemberAndLikedPost(final Member member,
                                           final Post post);
}