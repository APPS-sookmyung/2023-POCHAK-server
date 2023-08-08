package com.apps.pochak.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class ParentCommentDto {

    private String userProfileImg;
    private String userHandle;
    private LocalDateTime uploadedTime;
    private String content;
    private List<ChildCommentDto> childComments;

    @Builder
    public ParentCommentDto(String userProfileImg, String userHandle,
                            LocalDateTime uploadedTime, String content, List<ChildCommentDto> childComments) {
        this.userProfileImg = userProfileImg;
        this.userHandle = userHandle;
        this.uploadedTime = uploadedTime;
        this.content = content;
        this.childComments = childComments;
    }
}
