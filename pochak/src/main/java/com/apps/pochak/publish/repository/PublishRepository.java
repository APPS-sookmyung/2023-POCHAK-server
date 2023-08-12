package com.apps.pochak.publish.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.Status;
import com.apps.pochak.publish.domain.Publish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PublishRepository {
    private final PublishCrudRepository publishCrudRepository;
    private final DynamoDBMapper mapper;

    public Publish save(Publish publish) {
        return publishCrudRepository.save(publish);
    }

    /**
     * 다른 사람의 프로필에서 그 사람의 찍은 게시물들을 조회하는 경우 (only public)
     *
     * @param userHandle
     * @return
     * @throws BaseException
     */
    public List<Publish> findOnlyPublicPublishWithUserHandle(String userHandle) throws BaseException {

        // TODO: 이후 페이징 필요
        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");
        ean.put("#STATUS", "status");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("PUBLISH#"));
        eav.put(":val3", new AttributeValue().withS(Status.PUBLIC.toString()));

        DynamoDBQueryExpression<Publish> query = new DynamoDBQueryExpression<Publish>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withFilterExpression("#STATUS = :val3") // filter - get only public publish
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean)
                .withScanIndexForward(false); // desc

        List<Publish> publishes = mapper.query(Publish.class, query);
        return publishes;
    }

    /**
     * 자신의 프로필에서 모든 publish를 조회하는 경우 (private + public)
     *
     * @param userHandle
     * @return
     * @throws BaseException
     */
    public List<Publish> findAllPublishWithUserHandle(String userHandle) throws BaseException {

        // TODO: 이후 페이징 필요
        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("PUBLISH#"));

        DynamoDBQueryExpression<Publish> query = new DynamoDBQueryExpression<Publish>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean)
                .withScanIndexForward(false); // desc

        List<Publish> publishes = mapper.query(Publish.class, query);
        return publishes;
    }
}