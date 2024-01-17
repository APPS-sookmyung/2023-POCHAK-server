package com.apps.pochak.follow.domain.repository;

import com.apps.pochak.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
