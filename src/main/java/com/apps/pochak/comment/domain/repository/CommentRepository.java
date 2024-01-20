package com.apps.pochak.comment.domain.repository;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c " +
            "join fetch c.member " +
            "where c.post = :post " +
            "order by c.createdDate desc limit 1")
    Optional<Comment> findFirstByPost(@Param("post") final Post post);
}
