package com.apps.pochak.user.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.model.*;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.amazonaws.services.dynamodbv2.model.AttributeAction.ADD;
import static com.amazonaws.services.dynamodbv2.model.AttributeAction.DELETE;
import static com.apps.pochak.common.BaseResponseStatus.*;
import static com.apps.pochak.common.Status.PUBLIC;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserCrudRepository userCrudRepository;
    private final DynamoDBMapper mapper;
    private final AmazonDynamoDB amazonDynamoDB;

    public User findUserByUserHandle(String userHandle) throws BaseException {
        return userCrudRepository.findUserByHandleAndUserSKStartingWith(userHandle, "USER#")
                .orElseThrow(() -> new BaseException(INVALID_USER_HANDLE));
    }

    public User saveUser(User user) {
        user.setStatus(PUBLIC);
        return userCrudRepository.save(user);
    }

    public Optional<User> findUserWithSocialId(String socialId) {
        return userCrudRepository.findUserBySocialId(socialId);
    }

    /**
     * followingUserHandle이  followedUserHandle를 팔로우하고 있는지를 확인함.
     *
     * @param followedUserHandle
     * @param followingUserHandle
     * @return
     */
    public Boolean isFollow(String followedUserHandle, String followingUserHandle) {
        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");
        ean.put("#FOLLOWERS", "followerUserHandles");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(followedUserHandle));
        eav.put(":val2", new AttributeValue().withS("USER#"));
        eav.put(":val3", new AttributeValue().withS(followingUserHandle));

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
     * @param followedUser
     * @param followingUser
     * @param isFollow
     * @throws BaseException
     */
    public String followOrCancelByIsFollow(User followedUser, User followingUser, Boolean isFollow) throws BaseException {

        String followedUserHandle = followedUser.getHandle();
        String followingUserHandle = followingUser.getHandle();

        String result = "성공적으로 팔로우하였습니다."; // add
        AttributeAction action = ADD;
        if (isFollow) {
            result = "성공적으로 팔로우를 취소하였습니다."; // delete
            action = DELETE;
        }

        // add or delete follower
        // TODO: User SK가 "USER#" 이 아니라 다른것으로 바뀐다면 바꿔야 함.
        HashMap<String, AttributeValue> followerItemKey = new HashMap<>();
        followerItemKey.put("PartitionKey", new AttributeValue().withS(followedUserHandle));
        followerItemKey.put("SortKey", new AttributeValue().withS(followedUser.getUserSK()));

        HashMap<String, AttributeValueUpdate> followerUpdateValue = new HashMap<>();
        followerUpdateValue.put("followerUserHandles", new AttributeValueUpdate()
                .withValue(new AttributeValue().withSS(followingUserHandle))
                .withAction(action));

        UpdateItemRequest addFollower = new UpdateItemRequest()
                .withKey(followerItemKey)
                .withTableName("pochakdatabase")
                .withAttributeUpdates(followerUpdateValue);

        // add or delete following
        HashMap<String, AttributeValue> followingItemKey = new HashMap<>();
        followingItemKey.put("PartitionKey", new AttributeValue().withS(followingUserHandle));
        followingItemKey.put("SortKey", new AttributeValue().withS(followingUser.getUserSK()));

        HashMap<String, AttributeValueUpdate> followingUpdateValues = new HashMap<>();
        followingUpdateValues.put("followingUserHandles", new AttributeValueUpdate()
                .withValue(new AttributeValue().withSS(followedUserHandle))
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

    // TODO: user handle 중복 로직 처리(followers, followings 등)
    public void deleteUser(User user) throws BaseException {
        try {
            userCrudRepository.delete(user);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<User> batchGetUsers(List<String> userHandles) {

        List<KeyPair> keyPairList = userHandles.stream().map(
                userHandle -> {
                    return new KeyPair().withHashKey(userHandle).withRangeKey("USER#");
                }
        ).collect(Collectors.toList());

        Map<Class<?>, List<KeyPair>> keyPairForTable = new HashMap<>();
        keyPairForTable.put(User.class, keyPairList);

        Map<String, List<Object>> batchResults = mapper.batchLoad(keyPairForTable);
        List<Object> userList = batchResults.get("pochakdatabase");
        return (List<User>) (Object) userList;
    }
}
