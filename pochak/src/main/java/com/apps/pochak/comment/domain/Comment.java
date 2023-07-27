package com.apps.pochak.comment.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.apps.pochak.annotation.CustomGeneratedKey;
import com.apps.pochak.user.domain.UserId;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;

@DynamoDBTable(tableName = "pochakdatabase")
public class Comment {
    @Id
    private CommentId commentId;

    private String postPK;

    private String commentSK;

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.M)
    private UserId userId;

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.L)
    private List<CommentId> childComments = new ArrayList<>();

    @DynamoDBAttribute
    @Getter
    @Setter
    private String content;

    @DynamoDBHashKey(attributeName = "PartitionKey")
    public String getPostPK() {
        return commentId != null ? commentId.getPostPK() : null;
    }

    public void setPostPK(String postPK) {
        if (commentId == null) {
            commentId = new CommentId();
        }
        commentId.setPostPK(postPK);
    }

    @DynamoDBRangeKey(attributeName = "SortKey")
    @CustomGeneratedKey(prefix = "COMMENT#")
    public String getCommentSK() {
        return commentId != null ? commentId.getCommentSK() : null;
    }

    public void setCommentSK(String commentSK) {
        if (commentId == null) {
            commentId = new CommentId();
        }
        commentId.setCommentSK(commentSK);
    }

    public Comment(String postPK, UserId userId) {
        this.postPK = postPK;
        this.userId = userId;
    }
}
