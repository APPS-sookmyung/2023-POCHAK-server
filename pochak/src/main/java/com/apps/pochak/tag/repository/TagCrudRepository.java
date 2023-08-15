package com.apps.pochak.tag.repository;

import com.apps.pochak.tag.domain.Tag;
import com.apps.pochak.tag.domain.TagId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

import java.util.List;

@EnableScan
public interface TagCrudRepository extends DynamoDBCrudRepository<Tag, TagId> {
    List<Tag> findTagsByUserHandleAndStatus(String userHandle, String status);
}
