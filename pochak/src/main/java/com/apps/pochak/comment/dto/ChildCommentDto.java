package com.apps.pochak.comment.dto;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.user.domain.User;
import lombok.Builder;
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

    @Builder
    public ChildCommentDto(String userProfileImg, String userHandle, LocalDateTime uploadedTime, String content) {
        this.userProfileImg = userProfileImg;
        this.userHandle = userHandle;
        this.uploadedTime = uploadedTime;
        this.content = content;
    }


    public ChildCommentDto(User commentOwner, Comment newComment) {
        this.userProfileImg = commentOwner.getProfileImage();
        this.userHandle = commentOwner.getHandle();
        this.uploadedTime = LocalDateTime.parse(newComment.getUploadedDate().substring(7)); // 왜 string으로 반환하지?
        this.content = newComment.getContent();
    }
}
