package com.apps.pochak.user.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponseStatus;
import com.apps.pochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserCrudRepository userCrudRepository;
    private final DynamoDBMapper mapper;

    public User getUserWithUserPK(String userPK) throws BaseException {

        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
//        ean.put("#SK", "SortKey");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userPK));
//        eav.put(":val2", new AttributeValue().withS("USER#"));

        DynamoDBQueryExpression<User> query = new DynamoDBQueryExpression<User>()
                .withKeyConditionExpression("#PK = :val1")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);

        List<User> user = mapper.query(User.class, query);
        System.out.println("user test " + user.getClass());

        if (user.isEmpty()) {
            throw new BaseException(BaseResponseStatus.INVALID_USER_ID);
        }
        return user.get(0);
    }

    public User saveUser(User user) {
        return userCrudRepository.save(user);
    }
}
