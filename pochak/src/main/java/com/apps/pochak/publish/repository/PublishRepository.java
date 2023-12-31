package com.apps.pochak.publish.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.publish.domain.Publish;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.apps.pochak.common.Status.DELETED;
import static com.apps.pochak.common.Status.PUBLIC;

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
    public PublishData findOnlyPublicPublishWithUserHandle(String userHandle,
                                                           Map<String, AttributeValue> exclusiveStartKey)
            throws BaseException {

        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");
        ean.put("#STATUS", "status");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("PUBLISH#"));
        eav.put(":val3", new AttributeValue().withS(PUBLIC.toString()));

        DynamoDBQueryExpression<Publish> query = new DynamoDBQueryExpression<Publish>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withFilterExpression("#STATUS = :val3") // filter - get only public publish
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean)
                .withLimit(12)
                .withExclusiveStartKey(exclusiveStartKey)
                .withScanIndexForward(false); // desc

        return mapperQuery(query);
    }

    /**
     * 자신의 프로필에서 모든 publish를 조회하는 경우 (private + public)
     *
     * @param userHandle
     * @return
     * @throws BaseException
     */
    public PublishData findPublicAndPrivatePublishWithUserHandle(String userHandle,
                                                                 Map<String, AttributeValue> exclusiveStartKey)
            throws BaseException {

        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");
        ean.put("#STATUS", "status");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("PUBLISH#"));
        eav.put(":val3", new AttributeValue().withS(DELETED.toString()));

        DynamoDBQueryExpression<Publish> query = new DynamoDBQueryExpression<Publish>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withFilterExpression("#STATUS <> :val3")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean)
                .withLimit(12)
                .withExclusiveStartKey(exclusiveStartKey)
                .withScanIndexForward(false); // desc

        return mapperQuery(query);
    }

    public List<Publish> findPublicAndPrivatePublishWithUserHandleAndPostPK(String userHandle,
                                                                            String postPK) {
        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");
        ean.put("#STATUS", "status");
        ean.put("#POSTPK", "postPK");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("PUBLISH#"));
        eav.put(":val3", new AttributeValue().withS(DELETED.toString()));
        eav.put(":val4", new AttributeValue().withS(postPK));

        DynamoDBQueryExpression<Publish> query = new DynamoDBQueryExpression<Publish>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withFilterExpression("#STATUS <> :val3 AND #POSTPK = :val4")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);

        return mapperQuery(query).getResult();
    }

    public void deletePublicAndPrivatePublish(List<Publish> publishList) {
        for (Publish publish : publishList) {
            publish.setStatus(DELETED);
        }

        mapper.batchSave(publishList);
    }

    private PublishData mapperQuery(DynamoDBQueryExpression<Publish> query) {
        QueryResultPage<Publish> publishQueryResultPage = mapper.queryPage(Publish.class, query);
        Map<String, String> resultLastEvaluatedKey = (publishQueryResultPage.getLastEvaluatedKey() == null) ?
                null
                : publishQueryResultPage.getLastEvaluatedKey().entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().getS()
                        )
                );
        return new PublishData(publishQueryResultPage.getResults(), resultLastEvaluatedKey);
    }
    public Publish findPublicWithUserHandleAndPostPK(String userHandle, String postPK) {
        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");
        ean.put("#POSTPK", "postPK");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("PUBLISH#"));
        eav.put(":val3", new AttributeValue().withS(postPK));

        DynamoDBQueryExpression<Publish> query = new DynamoDBQueryExpression<Publish>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withFilterExpression("#POSTPK = :val3")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);

        return mapperQuery(query).getResult().get(0);
    }

    @Data
    @NoArgsConstructor
    public static class PublishData {
        private List<Publish> result;
        private Map<String, String> exclusiveStartKey;

        public PublishData(List<Publish> result, Map<String, String> exclusiveStartKey) {
            this.result = result;
            this.exclusiveStartKey = exclusiveStartKey;
        }
    }
}
