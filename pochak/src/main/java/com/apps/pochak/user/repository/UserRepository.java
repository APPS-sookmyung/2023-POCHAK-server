package com.apps.pochak.user.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.apps.pochak.common.BaseResponseStatus.INVALID_USER_HANDLE;
import static com.apps.pochak.common.BaseResponseStatus.INVALID_USER_ID;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserCrudRepository userCrudRepository;
    private final DynamoDBMapper mapper;

    public User findUserByUserId(UserId userId) throws BaseException {
        return userCrudRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_USER_ID));
    }

    public User findUserByUserHandle(String userHandle) throws BaseException {

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
            throw new BaseException(INVALID_USER_HANDLE);
        }
        return users.get(0);
    }

    public User saveUser(User user) {
        return userCrudRepository.save(user);
    }

    public Boolean isFollow(String userHandle, String loginUserHandle) {
        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");
        ean.put("#FOLLOWERS", "followerUserHandles");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("USER#"));
        eav.put(":val3", new AttributeValue().withS(loginUserHandle));

        DynamoDBQueryExpression<User> query = new DynamoDBQueryExpression<User>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withFilterExpression("contains (#FOLLOWERS, :val3)")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);

        List<User> result = mapper.query(User.class, query);
        if (result.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

//    public void follow(String userHandle, String loginUserHandle) {
//        HashMap<String, AttributeValue> itemKey = new HashMap<>();
//        itemKey.put("PartitionKey", new AttributeValue().withS(userHandle));
//        itemKey.put("SortKey", new AttributeValue().withS("USER#"));
//
//        HashMap<String, AttributeValueUpdate> updatedValues = new HashMap<>();
//        updatedValues.put("followerUserHandles", new AttributeValueUpdate()
//                .withValue(new AttributeValue().withS(loginUserHandle))
//                .withAction(AttributeAction.ADD));
//
//        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
//                .withKey(itemKey)
//                .withTableName("pochakdatabase")
//                .withAttributeUpdates(updatedValues);
//
//        DynamoDBSaveExpression dynamoDBSaveExpression = new DynamoDBSaveExpression()
//                .with;
//
//        mapper.
//
//        try {
//            ddb.updateItem(request);
//        } catch (ResourceNotFoundException e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        } catch (DynamoDbException e) {
//            System.err.println(e.getMessage());
//            System.exit(1);
//        }
//        System.out.println("The Amazon DynamoDB table was updated!");
//    }
}
