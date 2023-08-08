package com.apps.pochak.post.dto;

import com.apps.pochak.post.domain.Post;
import com.apps.pochak.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostUploadRequestDto {
    // testDto
    private String postImageUrl;
    private String caption;
    private List<String> taggedUserHandles;

    /**
     * ReqDto to Post Entity
     *
     * @param postOwner
     * @param taggedUsers
     * @return
     */
    public Post toEntity(User postOwner, List<User> taggedUsers) {
        return Post.builder()
                .owner(postOwner)
                .imgUrl(this.postImageUrl)
                .taggedUsers(taggedUsers)
                .caption(this.caption)
                .build();
    }
}
