package com.apps.pochak.post.dto;

import com.apps.pochak.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostElement {
    private Long postId;
    private String postImage;

    public static PostElement from(final Post post) {
        return new PostElement(post.getId(), post.getPostImage());
    }
}
