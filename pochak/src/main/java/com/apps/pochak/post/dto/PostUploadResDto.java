package com.apps.pochak.post.dto;

import com.apps.pochak.post.domain.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PostUploadResDto {

    // TODO: 포스트 업로드 후 동작을 알아야 할 듯
    private String postPK;

    private String postImageUrl;

    public PostUploadResDto(Post savedPost) {
        this.postPK = savedPost.getPostPK();
        this.postImageUrl = savedPost.getImgUrl();
    }
}
