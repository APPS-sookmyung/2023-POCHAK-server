package com.apps.pochak.user.repository;

import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.domain.UserId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

import java.util.Optional;

@EnableScan
public interface UserCrudRepository extends DynamoDBCrudRepository<User, UserId> {
    Optional<User> findBySocialId(String socialId);

    Optional<User> findUserByHandleAndUserSKStartingWith(String handle, String prefix);
}
