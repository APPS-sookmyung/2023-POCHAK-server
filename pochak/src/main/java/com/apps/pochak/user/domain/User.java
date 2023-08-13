package com.apps.pochak.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.apps.pochak.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType.SS;

@NoArgsConstructor
@DynamoDBTable(tableName = "pochakdatabase")
public class User extends BaseEntity {

    @Id // ID class should not have getter and setter.
    private UserId userId;

    /**
     * 표시되는 사용자 아이디
     */
    private String handle; // PK

    private String userSK; // SK

    @DynamoDBAttribute
    @Getter
    @Setter
    private String name;

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
    @DynamoDBTyped(SS)
    private Set<String> followingUserHandles = new HashSet<>();

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(SS)
    private Set<String> followerUserHandles = new HashSet<>();

    @Builder
    public User(String handle, String name, String message, String email, String profileImage) {
        this.setHandle("USER#" + handle); // PK와 SK는 setter 사용: ID에 저장하기 위해
        this.setUserSK(handle);
        this.setName(name);
        this.message = message;
        this.email = email;
        this.profileImage = profileImage;
    }

    @DynamoDBHashKey(attributeName = "PartitionKey")
    public String getHandle() {
        return userId != null ? userId.getHandle() : null; // prefix (#USER) 삭제한 값으로 반환
    }

    public void setHandle(String handle) {
        if (userId == null) {
            userId = new UserId();
        }
        userId.setHandle(handle);
    }

    // SK는 PK와 동일한 값으로 저장됨.
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

    public void updateUser(String profileImage, String name, String message) {
        this.profileImage = profileImage;
        this.name = name;
        this.message = message;
    }
}

