package com.apps.pochak.comment.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDeleteRequestDto {
    private LocalDateTime commentUploadedTime;
    private LocalDateTime parentCommentUploadedTime; // Allowing null values

    public String getDeletedCommentSK() {
        if (parentCommentUploadedTime != null) {
            return "COMMENT#CHILD#" + commentUploadedTime;
        }
        return "COMMENT#PARENT#" + commentUploadedTime;
    }

    public String getDeletedParentCommentSK() {
        if (parentCommentUploadedTime == null) {
            return null;
        }
        return "COMMENT#PARENT#" + parentCommentUploadedTime;
    }
}
