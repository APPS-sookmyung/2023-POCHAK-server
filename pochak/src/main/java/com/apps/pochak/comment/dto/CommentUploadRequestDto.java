package com.apps.pochak.comment.dto;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.post.domain.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CommentUploadRequestDto {

    private String content;
    private String parentCommentSK;

    // parent comment upload
    public Comment toEntity(Post post, String loginUserHandle, String uploadedDate) {
        return Comment.builder()
                .post(post)
                .content(this.content)
                .loginUserHandle(loginUserHandle)
                .uploadedDate(uploadedDate)
                .build();
    }
}
