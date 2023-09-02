package com.apps.pochak.comment.dto;

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
}
