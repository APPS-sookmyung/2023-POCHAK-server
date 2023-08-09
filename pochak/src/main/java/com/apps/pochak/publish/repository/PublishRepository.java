package com.apps.pochak.publish.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.apps.pochak.publish.domain.Publish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PublishRepository {
    private final PublishCrudRepository publishCrudRepository;
    private final DynamoDBMapper mapper;

    public Publish save(Publish publish) {
        return publishCrudRepository.save(publish);
    }
}
