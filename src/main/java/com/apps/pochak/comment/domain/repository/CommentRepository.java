package com.apps.pochak.comment.domain.repository;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query("update Comment c set c.status = 'DELETED' " +
            "where c.post = :post ")
    void bulkDeleteByPost(@Param("post") final Post post);

    @Query("select c from Comment c " +
            "join fetch c.member " +
            "where c.post = :post " +
            "order by c.createdDate desc limit 1")
    Optional<Comment> findFirstByPost(@Param("post") final Post post);

    @Query("select c from Comment c " +
            "join fetch c.member " +
            "where c.post = :post and c.parentComment is null ")
    Page<Comment> findParentCommentByPost(
            @Param("post") final Post post,
            Pageable pageable
    );

    @Query("select c from Comment c " +
            "join fetch c.member " +
            "where c.id = :commentId and c.parentComment is null ")
    Optional<Comment> findParentCommentById(
            @Param("commentId") final Long commentId
    );
}
