package com.apps.pochak.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CommentResDto {
    private List<ParentCommentDto> comments;
}
