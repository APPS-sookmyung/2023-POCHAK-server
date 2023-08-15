package com.apps.pochak.tag.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class TagRepository {
    private final TagCrudRepository tagCrudRepository;
    private final DynamoDBMapper mapper;

    public Tag save(Tag tag) {
        return tagCrudRepository.save(tag);
    }

    public List<Tag> findPublicTagsByUserHandle(String userHandle) throws BaseException {

        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("TAG#"));

        DynamoDBQueryExpression<Tag> query = new DynamoDBQueryExpression<Tag>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean)
                .withScanIndexForward(false); // desc

        List<Tag> tags = mapper.query(Tag.class, query);

        return tags;
    }
}
