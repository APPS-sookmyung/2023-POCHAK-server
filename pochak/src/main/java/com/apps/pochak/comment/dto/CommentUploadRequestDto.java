package com.apps.pochak.comment.dto;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.user.domain.User;

import java.util.List;

public class CommentUploadRequestDto {

    private String content;


    // child comment upload
    public Comment toEntity(String postPK,User loginUser, List<String> childCommentSKs){
        return Comment.builder()
                .postPK(postPK)
                .content(this.content)
                .loginUser(loginUser)
                .childCommentSKs(childCommentSKs)
                .build();
    }

    // parent comment upload
    public Comment toEntity(String postPK,User loginUser){
        return Comment.builder()
                .postPK(postPK)
                .content(this.content)
                .loginUser(loginUser)
                .build();
    }
}
