package com.apps.pochak.user.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.*;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amazonaws.services.dynamodbv2.model.AttributeAction.ADD;
import static com.amazonaws.services.dynamodbv2.model.AttributeAction.DELETE;
import static com.apps.pochak.common.BaseResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserCrudRepository userCrudRepository;
    private final DynamoDBMapper mapper;
    private final AmazonDynamoDB amazonDynamoDB;

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

    /**
     * follow update 수동 쿼리!!
     * isFollow == true : 팔로우 취소
     * isFollow == false : 팔로우
     *
     * @param userHandle
     * @param loginUserHandle
     * @param isFollow
     * @throws BaseException
     */
    public String followOrCancelByIsFollow(String userHandle, String loginUserHandle, Boolean isFollow) throws BaseException {

        // action 결정
        AttributeAction action = ADD;
        String result = "성공적으로 팔로우하였습니다.";

        if (isFollow) {
            action = DELETE;
            result = "성공적으로 팔로우를 취소하였습니다.";
        }

        // add or delete follower
        // TODO: User SK가 "USER#" 이 아니라 다른것으로 바뀐다면 바꿔야 함.
        HashMap<String, AttributeValue> followerItemKey = new HashMap<>();
        followerItemKey.put("PartitionKey", new AttributeValue().withS(userHandle));
        followerItemKey.put("SortKey", new AttributeValue().withS("USER#"));

        HashMap<String, AttributeValueUpdate> followerUpdateValue = new HashMap<>();
        followerUpdateValue.put("followerUserHandles", new AttributeValueUpdate()
                .withValue(new AttributeValue().withL(new AttributeValue().withS(loginUserHandle)))
                .withAction(action));

        UpdateItemRequest addFollower = new UpdateItemRequest()
                .withKey(followerItemKey)
                .withTableName("pochakdatabase")
                .withAttributeUpdates(followerUpdateValue);

        // add or delete following
        HashMap<String, AttributeValue> followingItemKey = new HashMap<>();
        followingItemKey.put("PartitionKey", new AttributeValue().withS(loginUserHandle));
        followingItemKey.put("SortKey", new AttributeValue().withS("USER#"));

        HashMap<String, AttributeValueUpdate> followingUpdateValues = new HashMap<>();
        followingUpdateValues.put("followingUserHandles", new AttributeValueUpdate()
                .withValue(new AttributeValue().withL(new AttributeValue().withS(userHandle)))
                .withAction(action));

        UpdateItemRequest addFollowing = new UpdateItemRequest()
                .withKey(followingItemKey)
                .withTableName("pochakdatabase")
                .withAttributeUpdates(followingUpdateValues);

        try {
            amazonDynamoDB.updateItem(addFollower);
            amazonDynamoDB.updateItem(addFollowing);
            return result;
        } catch (ResourceNotFoundException e) {
            throw new BaseException(RESOURCE_NOT_FOUND);
        } catch (AmazonDynamoDBException e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
