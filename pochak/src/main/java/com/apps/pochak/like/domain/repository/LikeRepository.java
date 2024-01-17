package com.apps.pochak.like.domain.repository;

import com.apps.pochak.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
