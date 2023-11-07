package com.apps.pochak.tag.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.tag.domain.Tag;
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
public class TagRepository {
    private final TagCrudRepository tagCrudRepository;
    private final DynamoDBMapper mapper;

    public Tag save(Tag tag) {
        return tagCrudRepository.save(tag);
    }

    public TagData findPublicTagsByUserHandle(String userHandle,
                                              Map<String, AttributeValue> exclusiveStartKey) throws BaseException {

        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");
        ean.put("#STATUS", "status");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("TAG#"));
        eav.put(":val3", new AttributeValue().withS(PUBLIC.toString()));

        DynamoDBQueryExpression<Tag> query = new DynamoDBQueryExpression<Tag>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withFilterExpression("#STATUS = :val3")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean)
                .withExclusiveStartKey(exclusiveStartKey)
                .withLimit(12) // TODO: 테스트 후 한번에 못 가져오면 개수 조정 필요
                .withScanIndexForward(false); // desc

        QueryResultPage<Tag> tagQueryResultPage = mapper.queryPage(Tag.class, query);
        Map<String, String> resultLastEvaluatedKey = (tagQueryResultPage.getLastEvaluatedKey() == null) ?
                null
                : tagQueryResultPage.getLastEvaluatedKey().entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().getS()
                        )
                );

        return new TagData(tagQueryResultPage.getResults(), resultLastEvaluatedKey);
    }

    public List<Tag> findPublicAndPrivateTagsByUserHandleAndPostPK(String userHandle, String postPK) {

        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");
        ean.put("#STATUS", "status");
        ean.put("#POSTPK", "postPK");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("TAG#"));
        eav.put(":val3", new AttributeValue().withS(DELETED.toString()));
        eav.put(":val4", new AttributeValue().withS(postPK));

        DynamoDBQueryExpression<Tag> query = new DynamoDBQueryExpression<Tag>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withFilterExpression("#STATUS <> :val3 and #POSTPK = :val4")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean);

        QueryResultPage<Tag> tagQueryResultPage = mapper.queryPage(Tag.class, query);

        return tagQueryResultPage.getResults();
    }

    public void deletePublicAndPrivateTagsByUserHandleAndPostPK(List<Tag> tagList) {
        for (Tag tag : tagList) {
            tag.setStatus(DELETED);
        }
        mapper.batchSave(tagList);
    }

    @Data
    @NoArgsConstructor
    public static class TagData {
        private List<Tag> result;
        private Map<String, String> exclusiveStartKey;

        public TagData(List<Tag> result, Map<String, String> resultLastEvaluatedKey) {
            this.result = result;
            this.exclusiveStartKey = resultLastEvaluatedKey;
        }
    }
}
