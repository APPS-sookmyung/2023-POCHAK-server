package com.apps.pochak.post.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.apps.pochak.annotation.CustomGeneratedKey;
import com.apps.pochak.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;

@DynamoDBTable(tableName = "pochakdatabase")
public class Post extends BaseEntity {
    @Id
    private PostId postId;
    private String postPK;

    private String postSK;

    /**
     * User Handle
     */
    @DynamoDBAttribute
    @Getter
    @Setter
    private String owner;

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.L)
    private List<String> taggedUserHandles = new ArrayList<>();

    @DynamoDBAttribute
    @Getter
    @Setter
    private String imgUrl;

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.L)
    private List<String> likeUserHandles = new ArrayList<>();

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.L)
    private List<String> parentCommentSKs = new ArrayList<>();

    @DynamoDBAttribute
    @Getter
    @Setter
    private String caption;

    @DynamoDBHashKey(attributeName = "PartitionKey")
    @CustomGeneratedKey(prefix = "POST#")
    public String getPostPK() {
        return postId != null ? postId.getPostPK() : null;
    }

    public void setPostPK(String postPK) {
        if (postId == null) {
            postId = new PostId();
        }
        postId.setPostPK(postPK);
    }

    @DynamoDBRangeKey(attributeName = "SortKey")
    @CustomGeneratedKey(prefix = "POST#")
    public String getPostSK() {
        return postId != null ? postId.getPostSK() : null;
    }

    public void setPostSK(String postSK) {
        if (postId == null) {
            postId = new PostId();
        }
        postId.setPostSK(postSK);
    }
}
