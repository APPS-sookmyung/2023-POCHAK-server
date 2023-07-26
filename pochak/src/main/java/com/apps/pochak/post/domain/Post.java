package com.apps.pochak.post.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.apps.pochak.annotation.CustomGeneratedKey;
import com.apps.pochak.comment.domain.CommentId;
import com.apps.pochak.common.BaseEntity;
import com.apps.pochak.user.domain.UserId;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@DynamoDBTable(tableName = "pochakdb")
public class Post extends BaseEntity {
    @Id
    private PostId postId;
    private String postPK;

    private String postSK;

    @DynamoDBAttribute
    @Getter
    @Setter
    private UserId owner;
    @DynamoDBAttribute
    @Getter
    @Setter
    private List<String> imgUrls;
    @DynamoDBAttribute
    @Getter
    @Setter
    private List<UserId> likeUsers;
    @DynamoDBAttribute
    @Getter
    @Setter
    private List<CommentId> parentComments;
    @DynamoDBAttribute
    @Getter
    @Setter
    private String caption;

    @DynamoDBHashKey(attributeName = "Partition key")
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

    @DynamoDBRangeKey(attributeName = "Sort Key")
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
