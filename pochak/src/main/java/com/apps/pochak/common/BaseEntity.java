package com.apps.pochak.common;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static com.apps.pochak.common.config.DynamoDBConfig.LocalDateTimeConverter;

@Getter
@Setter //Setters are used in aws-dynamodb-sdk
@NoArgsConstructor
@DynamoDBDocument
public class BaseEntity {
    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdDate = LocalDateTime.now(); // @CreatedDate가 작동되지 않아 그냥 now() 씀

    @LastModifiedDate
    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    private LocalDateTime lastModifiedDate;

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute
    private Status status = Status.PRIVATE; // default
}
