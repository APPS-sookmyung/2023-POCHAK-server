package com.apps.pochak.user.repository;

import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.domain.UserId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

@EnableScan
public interface UserCrudRepository extends DynamoDBCrudRepository<User, UserId> {
}
