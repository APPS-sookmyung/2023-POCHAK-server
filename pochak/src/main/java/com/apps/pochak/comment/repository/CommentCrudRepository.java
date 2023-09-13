package com.apps.pochak.comment.repository;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.domain.CommentId;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface CommentCrudRepository extends CrudRepository<Comment, CommentId> {
    // LocalDateTime 자리수 고려 - StartingWith로 설정.
    Optional<Comment> findCommentByPostPKAndUploadedDateStartingWith(String postPK, String commentSKPrefix);
}
