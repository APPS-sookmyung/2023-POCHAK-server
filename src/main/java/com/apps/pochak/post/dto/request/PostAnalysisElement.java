package com.apps.pochak.post.dto.request;

import com.apps.pochak.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostAnalysisElement {
    private String postId;
    private String caption;
    private String postImage;

    @Builder(builderMethodName = "from")
    public PostAnalysisElement(Post post) {
        this.postId = post.getId().toString();
        this.caption = post.getCaption();
        this.postImage = post.getPostImage();
    }
}
