package com.apps.pochak.comment.dto;

import com.apps.pochak.comment.domain.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ParentCommentDto {

    private String userProfileImg;
    private String userHandle;
    private LocalDateTime uploadedTime;
    private String content;
    private RecentCommentDto recentComment;

    @Data
    public static class RecentCommentDto {
        private String childCommentProfileImg;
        private String content;

        public RecentCommentDto(String childCommentProfileImg, String content) {
            this.childCommentProfileImg = childCommentProfileImg;
            this.content = content;
        }
    }

    public ParentCommentDto(Comment comment) {
        this.userProfileImg = comment.getCommentUserProfileImage();
        this.userHandle = comment.getCommentUserHandle();
        this.uploadedTime = comment.getCreatedDate();
        this.content = comment.getContent();
        if (comment.getRecentChildCommentSK() != null) {
            this.recentComment = new RecentCommentDto(
                    comment.getRecentChildCommentProfileImage(),
                    comment.getRecentChildCommentContent());
        }
    }
}
