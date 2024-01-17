package com.apps.pochak.likes.domain.repository;

import com.apps.pochak.likes.domain.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
}
