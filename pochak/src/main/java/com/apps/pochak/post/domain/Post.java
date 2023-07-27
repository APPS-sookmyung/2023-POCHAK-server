package com.apps.pochak.post.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.apps.pochak.annotation.CustomGeneratedKey;
import com.apps.pochak.comment.domain.CommentId;
import com.apps.pochak.common.BaseEntity;
import com.apps.pochak.user.domain.UserId;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.*;

@DynamoDBTable(tableName = "pochakdatabase")
public class Post extends BaseEntity {
    @Id
    private PostId postId;
    private String postPK;

    private String postSK;

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.M)
    private UserId owner;
    @DynamoDBAttribute
    @Getter
    @Setter
    private List<String> imgUrls = new ArrayList<>();
    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.M)
    private List<UserId> likeUsers = new ArrayList<>();
    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.M)
    private List<CommentId> parentComments = new ArrayList<>();
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
