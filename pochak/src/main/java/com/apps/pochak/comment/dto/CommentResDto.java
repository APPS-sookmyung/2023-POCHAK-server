package com.apps.pochak.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CommentResDto {
    private String loginProfileImg;

    private List<ParentCommentDto> comments;

    public CommentResDto(List<ParentCommentDto> comments) {
        this.comments = comments;
    }
}
