package com.apps.pochak.comment.dto;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.post.domain.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CommentResDto {

    private List<ParentCommentDto> comments;



    public CommentResDto(List<ParentCommentDto> comments){
        this.comments=comments;
    }

}
