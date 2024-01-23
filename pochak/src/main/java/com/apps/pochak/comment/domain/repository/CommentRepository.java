package com.apps.pochak.comment.domain.repository;

import com.apps.pochak.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query("""
            UPDATE Comment comment
            SET comment.status = 'DELETED'
            WHERE comment.member.id = :memberId or comment.post.owner.id = :memberId
            """)
    void deleteCommentByMemberId(@Param("memberId") final Long memberId);
}
