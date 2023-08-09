package com.apps.pochak.publish.repository;

import com.apps.pochak.publish.domain.Publish;
import com.apps.pochak.publish.domain.PublishId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

@EnableScan
public interface PublishCrudRepository extends DynamoDBCrudRepository<Publish, PublishId> {
}
