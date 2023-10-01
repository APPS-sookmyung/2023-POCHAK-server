package com.apps.pochak.comment.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDeleteRequestDto {
    // Comment SK = COMMENT# + (LocalDateTime) UploadedTime
    private LocalDateTime commentUploadedTime;
    private LocalDateTime commentParentUploadedTime; // Allowing null values

    public String getDeletedCommentSK() {
        return "COMMENT#" + commentUploadedTime;
    }

    public String getDeletedCommentParentSK() {
        return "COMMENT#" + commentParentUploadedTime;
    }
}
