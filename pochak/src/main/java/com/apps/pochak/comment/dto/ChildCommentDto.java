package com.apps.pochak.comment.dto;

import com.apps.pochak.comment.domain.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ChildCommentDto {
    private String userProfileImg;
    private String userHandle;
    private LocalDateTime uploadedTime;
    private String content;

    public ChildCommentDto(Comment comment) {
        this.userProfileImg = comment.getCommentUserProfileImage();
        this.userHandle = comment.getCommentUserHandle();
        this.uploadedTime = comment.getCreatedDate();
        this.content = comment.getContent();
    }
}
