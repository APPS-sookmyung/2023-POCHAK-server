package com.apps.pochak.comment.repository;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.comment.domain.CommentId;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface CommentCrudRepository extends CrudRepository<Comment, CommentId> {
}
