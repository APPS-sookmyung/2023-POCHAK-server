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

    public ParentCommentDto(User commentOwner, Comment newComment) {
        this.userProfileImg = commentOwner.getProfileImage();
        this.userHandle = commentOwner.getHandle();
        this.uploadedTime = LocalDateTime.parse(newComment.getUploadedDate().substring(7)); // 왜 string으로 반환하지?
        this.content = newComment.getContent();
    }

    // child comment가 있는 경우
    public ParentCommentDto(User commentOwner, Comment newComment, List<ChildCommentDto> childComments) {
        this.userProfileImg = commentOwner.getProfileImage();
        this.userHandle = commentOwner.getHandle();
        this.uploadedTime = LocalDateTime.parse(newComment.getUploadedDate().substring(7)); // 왜 string으로 반환하지?
        this.content = newComment.getContent();
        this.childComments = childComments;
    }

}
