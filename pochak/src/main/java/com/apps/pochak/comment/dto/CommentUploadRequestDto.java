package com.apps.pochak.comment.dto;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentUploadRequestDto {

    private String content;
    private String parentCommentSK;

    public Comment toEntity(Post post, User user, String uploadedDate) {
        return Comment.builder()
                .post(post)
                .content(this.content)
                .commentUserProfileImage(user.getProfileImage())
                .loginUserHandle(user.getHandle())
                .uploadedDate(uploadedDate)
                .parentCommentSK(this.parentCommentSK)
                .build();
    }
}
