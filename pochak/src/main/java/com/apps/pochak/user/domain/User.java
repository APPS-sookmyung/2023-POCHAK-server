package com.apps.pochak.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.apps.pochak.annotation.CustomGeneratedKey;
import com.apps.pochak.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;

@NoArgsConstructor
@DynamoDBTable(tableName = "pochakdatabase")
public class User extends BaseEntity {

    @Id // ID class should not have getter and setter.
    private UserId userId;
    private String userPK;
    private String userSK;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String name;

    // 표시되는 사용자 아이디
    @DynamoDBAttribute
    @Getter
    @Setter
    private String handle;

    // 한 줄 소개
    @DynamoDBAttribute
    @Getter
    @Setter
    private String message;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String email;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String profileImage;

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.L)
    private List<UserId> followingList = new ArrayList<>();

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.L)
    private List<UserId> followerList = new ArrayList<>();

    @CustomGeneratedKey(prefix = "USER#")
    @DynamoDBHashKey(attributeName = "PartitionKey")
    public String getUserPK() {
        return userId != null ? userId.getUserPK() : null;
    }

    public void setUserPK(String userPK) {
        if (userId == null) {
            userId = new UserId();
        }
        userId.setUserPK(userPK);
    }

    @CustomGeneratedKey(prefix = "USER#")
    @DynamoDBRangeKey(attributeName = "SortKey")
    public String getUserSK() {
        return userId != null ? userId.getUserSK() : null;
    }

    public void setUserSK(String userSK) {
        if (userId == null) {
            userId = new UserId();
        }
        userId.setUserSK(userSK);
    }
}

