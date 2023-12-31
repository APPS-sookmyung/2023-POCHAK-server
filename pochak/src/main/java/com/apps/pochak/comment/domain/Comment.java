package com.apps.pochak.comment.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.apps.pochak.common.BaseEntity;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.user.domain.User;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel.DynamoDBAttributeType;

@NoArgsConstructor

@AllArgsConstructor


@DynamoDBTable(tableName = "pochakdatabase")
public class Comment extends BaseEntity {
    @Id
    private CommentId commentId;

    private String postPK;

    private String uploadedDate;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String commentUserProfileImage;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String commentUserHandle;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String parentCommentSK;

    @DynamoDBAttribute
    @Getter
    @Setter
    @DynamoDBTyped(DynamoDBAttributeType.L)
    private List<String> childCommentSKs = new ArrayList<>();

    @DynamoDBAttribute
    @Getter
    @Setter
    private String recentChildCommentSK;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String recentChildCommentProfileImage;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String recentChildCommentContent;

    @DynamoDBAttribute
    @Getter
    @Setter
    private String content;

    @Builder
    public Comment(Post post, String commentUserProfileImage, String loginUserHandle, String content, String uploadedDate, String parentCommentSK) {
        // PK, SK의 setter 사용 유의
        this.setPostPK(post.getPostPK());
        this.setUploadedDate(uploadedDate);
        this.commentUserProfileImage = commentUserProfileImage;
        this.commentUserHandle = loginUserHandle;
        this.content = content;
        this.parentCommentSK = parentCommentSK;
    }

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
    public String getUploadedDate() {
        return commentId != null ? commentId.getUploadedDate() : null;
    }

    /**
     * 사용 유의! 앞에 Prefix(COMMENT#CHILD# or COMMENT#PARENT#) 붙어있어야 함.
     *
     * @param uploadedDate
     */
    public void setUploadedDate(String uploadedDate) {
        if (commentId == null) {
            commentId = new CommentId();
        }
        commentId.setUploadedDate(uploadedDate);
    }

    public void setUploadedDate(LocalDateTime uploadedDate) {
        if (commentId == null) {
            commentId = new CommentId();
        }
        commentId.setUploadedDate("COMMENT#" + uploadedDate);
    }

    public void setRecentChildComment(Comment recentChildComment, User user) {
        this.recentChildCommentSK = recentChildComment.getUploadedDate();
        this.recentChildCommentProfileImage = user.getProfileImage();
        this.recentChildCommentContent = recentChildComment.getContent();
    }
}
