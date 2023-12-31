package com.apps.pochak.post.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.apps.pochak.annotation.CustomGeneratedKey;
import com.apps.pochak.common.BaseEntity;
import com.apps.pochak.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;

@NoArgsConstructor
@DynamoDBTable(tableName = "pochakdatabase")
public class Post extends BaseEntity {
    @Id
    private PostId postId;
    private String postPK;
    private String allowedDate;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String ownerHandle;

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

    @Builder
    public Post(User owner, List<String> taggedUsersHandles, String imgUrl, String caption) {
        this.ownerHandle = owner.getHandle();
        this.taggedUserHandles = taggedUsersHandles;
        this.imgUrl = imgUrl;
        this.caption = caption;
        this.setAllowedDate("POST#");
    }

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
    public String getAllowedDate() {
        return postId != null ? postId.getAllowedDate() : null;
    }

    /**
     * 사용 유의! 앞에 Prefix(POST#) 붙어있어야 함.
     *
     * @param allowedDate
     */
    public void setAllowedDate(String allowedDate) {
        if (postId == null) {
            postId = new PostId();
        }
        postId.setAllowedDate(allowedDate);
    }

    public void setAllowedDate(LocalDateTime allowedDate) {
        if (postId == null) {
            postId = new PostId();
        }
        postId.setAllowedDate("POST#" + allowedDate);
    }
}
