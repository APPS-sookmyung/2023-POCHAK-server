package com.apps.pochak.post.repository;

import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.domain.PostId;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface PostRepository extends CrudRepository<Post, PostId> {
}
