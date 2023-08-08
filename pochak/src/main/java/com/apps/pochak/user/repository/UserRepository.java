package com.apps.pochak.user.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.apps.pochak.common.BaseResponseStatus.INVALID_USER_ID;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserCrudRepository userCrudRepository;
    private final DynamoDBMapper mapper;

    public User findUserWithUserId(UserId userId) throws BaseException {
        return userCrudRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_ID));
    }

    public User findUserWithUserHandle(String userHandle) throws BaseException {

        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("USER#"));

        DynamoDBQueryExpression<User> query = new DynamoDBQueryExpression<User>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);

        List<User> users = mapper.query(User.class, query);

        if (users.isEmpty()) {
            throw new BaseException(INVALID_USER_ID);
        }
        return users.get(0);
    }

    public User saveUser(User user) {
        return userCrudRepository.save(user);
    }

    public Optional<User> findUserWithSocialId(String socialId) {
        return userCrudRepository.findBySocialId(socialId);
    }

    public Optional<User> findUserWithRefreshToken(String refreshToken) {
        return userCrudRepository.findByRefreshToken(refreshToken);
    }

    public void updateUser(User user) {
        mapper.save(user, new DynamoDBSaveExpression().withExpectedEntry("PartitionKey", new ExpectedAttributeValue(new AttributeValue().withS(user.getHandle()))));
    }
}
