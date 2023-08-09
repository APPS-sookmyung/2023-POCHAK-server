package com.apps.pochak.tag.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagRepository {
    private final TagCrudRepository tagCrudRepository;
    private final DynamoDBMapper mapper;
}
