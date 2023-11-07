package com.apps.pochak.alarm.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.apps.pochak.alarm.domain.Alarm;
import com.apps.pochak.common.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.apps.pochak.common.Status.*;

@Repository
@RequiredArgsConstructor
public class AlarmRepository {
    private final AlarmCrudRepository alarmCrudRepository;
    private final DynamoDBMapper mapper;

    public List<Alarm> findAllPublicAlarmsWithUserHandle(String userHandle) {
        HashMap<String, String> ean = new HashMap<>();
        ean.put("#PK", "PartitionKey");
        ean.put("#SK", "SortKey");
        ean.put("#STATUS", "status");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(userHandle));
        eav.put(":val2", new AttributeValue().withS("ALARM#"));
        eav.put(":val3", new AttributeValue().withS(PUBLIC.toString()));

        DynamoDBQueryExpression<Alarm> query = new DynamoDBQueryExpression<Alarm>()
                .withKeyConditionExpression("#PK = :val1 and begins_with(#SK, :val2)")
                .withFilterExpression("#STATUS = :val3") // filter - get only public alarm
                .withExpressionAttributeValues(eav)
                .withExpressionAttributeNames(ean)
                .withScanIndexForward(false); // desc

        return mapper.query(Alarm.class, query);
    }


}
