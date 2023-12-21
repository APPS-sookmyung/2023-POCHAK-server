package com.apps.pochak.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.apps.pochak.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType.S;
import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType.SS;

@NoArgsConstructor
@DynamoDBTable(tableName = "pochakdatabase")
public class User extends BaseEntity {

    @Id // ID class should not have getter and setter.
    private UserId userId;

    private String handle; // PK

    private String userSK; // SK

    @DynamoDBAttribute
    @Getter
    @Setter
    private String name;

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
    private String refreshToken;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String socialId;

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(S)
    private SocialType socialType;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String socialRefreshToken;

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(SS)
    private Set<String> followingUserHandles = new HashSet<>(Arrays.asList(""));

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(SS)
    private Set<String> followerUserHandles = new HashSet<>(Arrays.asList(""));

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

    @DynamoDBIgnore
    public Set<String> getValidFollowingSet() {
        if (!this.isEmptyFollowingSet()) {
            HashSet<String> removeNullValueFollowingSet = new HashSet<>(this.followingUserHandles);
            removeNullValueFollowingSet.remove("");
            return removeNullValueFollowingSet;
        }
        return new HashSet<>();
    }

    @DynamoDBIgnore
    public Set<String> getValidFollowerSet() {
        if (!this.isEmptyFollowerSet()) {
            HashSet<String> removeNullValueFollowerSet = new HashSet<>(this.followerUserHandles);
            removeNullValueFollowerSet.remove("");
            return removeNullValueFollowerSet;
        }
        return new HashSet<>();
    }

    public void updateUser(String profileImage, String name, String message) {
        this.profileImage = profileImage;
        this.name = name;
        this.message = message;
    }

    @Builder(builderMethodName = "signupUser", builderClassName = "signupUser")
    public User(String name, String email, String handle, String message, String socialId, SocialType socialType, String profileImage, String socialRefreshToken) {
        this.setHandle(handle);
        this.setUserSK(getUserSK());
        this.name = name;
        this.email = email;
        this.message = message;
        this.socialId = socialId;
        this.socialType = socialType;
        this.profileImage = profileImage;
        this.socialRefreshToken = socialRefreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @DynamoDBIgnore
    public Boolean isEmptyFollowerSet() {
        return this.followerUserHandles.size() == 1 && this.followerUserHandles.contains("");
    }

    @DynamoDBIgnore
    public Boolean isEmptyFollowingSet() {
        return this.followingUserHandles.size() == 1 && this.followingUserHandles.contains("");
    }

    @DynamoDBIgnore
    public Integer getFollowerCount() {
        final int NULL_VALUE = 1;
        if (this.isEmptyFollowerSet()) {
            return 0;
        } else return this.followerUserHandles.size() - NULL_VALUE;
    }

    @DynamoDBIgnore
    public Integer getFollowingCount() {
        final int NULL_VALUE = 1;
        if (this.isEmptyFollowingSet()) {
            return 0;
        } else return this.followingUserHandles.size() - NULL_VALUE;
    }
}

