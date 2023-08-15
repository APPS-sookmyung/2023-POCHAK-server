package com.apps.pochak.tag.repository;

import com.apps.pochak.tag.domain.Tag;
import com.apps.pochak.tag.domain.TagId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

@EnableScan
public interface TagCrudRepository extends DynamoDBCrudRepository<Tag, TagId> {
}
