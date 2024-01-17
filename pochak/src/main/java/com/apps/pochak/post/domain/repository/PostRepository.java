package com.apps.pochak.post.domain.repository;

import com.apps.pochak.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
