package com.apps.pochak.post.dto;


import com.apps.pochak.post.domain.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostLikeResDto {
    // TODO : 좋아요 누르기 이후 무엇을 전달해주어야 하는지 확인 이후 추가예정
    private String postPK;
    private String postImageUrl;



    public PostLikeResDto(Post likedPost){
        this.postPK=likedPost.getPostPK();
        this.postImageUrl=likedPost.getImgUrl();
    }
}
