package com.apps.pochak.like.domain.repository;

import com.apps.pochak.like.domain.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
}
