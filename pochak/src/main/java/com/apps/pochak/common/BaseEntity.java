package com.apps.pochak.common;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static com.apps.pochak.common.DynamoDBConfig.LocalDateTimeConverter;

@Getter
@Setter //Setters are used in aws-dynamodb-sdk
@NoArgsConstructor
public class BaseEntity {
    @CreatedDate
    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    private LocalDateTime lastModifiedDate;

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute
    private Status status = Status.PRIVATE; // default
}
