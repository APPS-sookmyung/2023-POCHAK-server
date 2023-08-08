package com.apps.pochak.post.dto;

import com.apps.pochak.post.domain.Post;
import com.apps.pochak.user.domain.UserId;
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

    public Post toEntity(UserId loginUser, List<UserId> taggedUsers) {
        Post post = new Post();
        post.setOwner(loginUser);
        post.setTaggedUsers(taggedUsers);
        post.setImgUrl(this.postImageUrl);
        post.setCaption(this.caption);
        return post;
    }
}
