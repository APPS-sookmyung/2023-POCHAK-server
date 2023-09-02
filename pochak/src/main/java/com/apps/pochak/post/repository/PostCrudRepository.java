package com.apps.pochak.post.repository;

import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.domain.PostId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

import java.util.Optional;

@EnableScan
public interface PostCrudRepository extends DynamoDBCrudRepository<Post, PostId> {
    Optional<Post> findPostByPostPKAndAllowedDateStartingWith(String postPK, String prefix);
}
