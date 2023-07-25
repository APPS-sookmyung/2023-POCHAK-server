package com.apps.pochak.user.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.apps.pochak.annotation.CustomGeneratedKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@DynamoDBTable(tableName = "pochakdb")
public class User {

    @Id
    private UserId userId;
    private String userPK;
    private String userSK;

    @DynamoDBAttribute
    @Getter @Setter
    private String name;

    // 표시되는 사용자 아이디
    @DynamoDBAttribute
    @Getter @Setter
    private String nickname;

    // 한 줄 소개
    @DynamoDBAttribute
    @Getter @Setter
    private String message;

    @DynamoDBAttribute
    @Getter @Setter
    private String email;

    @DynamoDBAttribute
    @Getter @Setter
    private String profileImage;

    @DynamoDBAttribute
    @Getter @Setter
    private List<UserId> followingList;

    @DynamoDBAttribute
    @Getter @Setter
    private List<UserId> followerList;

    @CustomGeneratedKey(prefix = "USER#")
    @DynamoDBHashKey(attributeName = "Partition key")
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
    @DynamoDBRangeKey(attributeName = "Sort Key")
    public String getUserSK() {
        return userId != null ? userId.getUserSK() : null;
    }

    /**
     * 사용할 일 없는 메소드 - userPK와 userSK는 동일해야 함.
     * @param userSK
     */
    public void setUserSK(String userSK) {
        if (userId == null) {
            userId = new UserId();
        }
        userId.setUserSK(userSK);
    }
}

