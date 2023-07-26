package com.apps.pochak.user.repository;

import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.domain.UserId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableScan
public interface UserRepository extends DynamoDBCrudRepository<User, UserId> {

    /**
     * 여기 @Query 사용법 찾아보기
     * Partition Key = :userPK AND BEGINS_WITH(Sort Key, "USER#")
     * 사용법을 모르겠음
     */
//    @Query(fields = "")
    List<User> findUserByUserPK(@Param("userPK") String userPK);

    User save(User user);
}
