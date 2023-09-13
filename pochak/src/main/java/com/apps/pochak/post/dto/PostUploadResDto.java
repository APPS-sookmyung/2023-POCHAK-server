package com.apps.pochak.post.dto;

import com.apps.pochak.post.domain.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PostUploadResDto {
    private String postPK;
    private String postImageUrl;

    public PostUploadResDto(Post savedPost) {
        this.postPK = savedPost.getPostPK();
        this.postImageUrl = savedPost.getImgUrl();
    }
}
