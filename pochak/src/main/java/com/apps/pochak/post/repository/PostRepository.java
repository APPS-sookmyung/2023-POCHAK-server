package com.apps.pochak.post.repository;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.domain.PostId;
import lombok.RequiredArgsConstructor;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final PostCrudRepository postCrudRepository;
    private final DynamoDBMapper mapper;
    public Post savePost(Post post){
        return postCrudRepository.save(post);
    }
}



