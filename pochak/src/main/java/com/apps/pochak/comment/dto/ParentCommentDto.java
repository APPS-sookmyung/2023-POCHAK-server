package com.apps.pochak.comment.dto;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.user.domain.User;
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

    public ParentCommentDto(User loginUser, Comment newComment){
        this.userProfileImg = loginUser.getProfileImage();
        this.userHandle = loginUser.getHandle();
        this.uploadedTime = newComment.getUploadedDate(); // 왜 date로 되어있는지?
        this.content = newComment.getContent();

    }

}
