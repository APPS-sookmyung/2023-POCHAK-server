package com.apps.pochak.comment.dto;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CommentResDto {
    private String loginProfileImg;

    private List<ParentCommentDto> comments;

    public CommentResDto(User loginUser, List<Comment> comments) {
        this.loginProfileImg = loginUser.getProfileImage();
        this.comments = comments.stream().map(
                comment -> new ParentCommentDto(comment)
        ).collect(Collectors.toList());
    }
}
