package com.apps.pochak.tag.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.apps.pochak.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagRepository {
    private final TagCrudRepository tagCrudRepository;
    private final DynamoDBMapper mapper;

    public Tag save(Tag tag) {
        return tagCrudRepository.save(tag);
    }
}
