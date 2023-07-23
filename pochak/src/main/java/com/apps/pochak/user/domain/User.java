package com.apps.pochak.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.apps.pochak.annotation.CustomGeneratedKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "pochakdb")
public class User {
    private String userPK;

    @DynamoDBHashKey(attributeName = "Partition key")
    @CustomGeneratedKey(prefix = "USER#")
    public String getUserPK() {
        return this.userPK;
    }

    @DynamoDBRangeKey(attributeName = "Sort Key")
    private String userSK = "USER";

    @DynamoDBAttribute
    private String name;

    // 표시되는 사용자 아이디
    @DynamoDBAttribute
    private String nickname;

    // 한 줄 소개
    @DynamoDBAttribute
    private String message;

    @DynamoDBAttribute
    private String email;

    @DynamoDBAttribute
    private String profileImage;

    @DynamoDBAttribute
    private List<String> followingList;

    @DynamoDBAttribute
    private List<String> followerList;

}

