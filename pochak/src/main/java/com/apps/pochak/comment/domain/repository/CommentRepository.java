package com.apps.pochak.comment.domain.repository;

import com.apps.pochak.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
