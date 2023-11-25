package com.apps.pochak.post.dto;

import com.apps.pochak.post.domain.Post;
import com.apps.pochak.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class PostUploadRequestDto {

    private MultipartFile postImage;
    private String caption;
    private List<String> taggedUserHandles;

    /**
     * ReqDto to Post Entity
     *
     * @param postOwner
     * @return
     */
    public Post toEntity(User postOwner, String postImageUrl) {
        return Post.builder()
                .owner(postOwner)
                .imgUrl(postImageUrl)
                .taggedUsersHandles(this.taggedUserHandles)
                .caption(this.caption)
                .build();
    }
}
