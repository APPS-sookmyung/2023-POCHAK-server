package com.apps.pochak.comment.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentDeleteRequestDto {
    private String commentSK;
    private String parentCommentSK;
}
