package com.apps.pochak.tag.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepository {
    private final TagCrudRepository tagCrudRepository;
    private final DynamoDBMapper mapper;

    public Tag save(Tag tag) {
        return tagCrudRepository.save(tag);
    }

    public List<Tag> findPublicTagsByUserHandle(String userHandle) throws BaseException {

        return tagCrudRepository.findTagsByUserHandleAndStatus(userHandle, "PUBLIC");
    }
}
