package com.apps.pochak.Tag.repository;

import com.apps.pochak.Tag.domain.Tag;
import com.apps.pochak.Tag.domain.TagId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

@EnableScan
public interface TagCrudRepository extends DynamoDBCrudRepository<Tag, TagId> {
}
