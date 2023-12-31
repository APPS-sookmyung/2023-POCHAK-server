package com.apps.pochak.post.dto;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostDetailResDto {
    private List<String> taggedUserHandles;
    private String postOwnerProfileImage;
    private String postOwnerHandle;
    private Boolean isFollow;
    private Boolean isLike;
    private String postImageUrl;
    private int numOfHeart;
    private String caption;
    private MainComment mainComment;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainComment {
        private String userHandle;
        private String content;
    }

    public PostDetailResDto(Post post, User owner, User loginUser, Boolean isFollow, Comment randomComment) {
        this.taggedUserHandles = post.getTaggedUserHandles();
        this.postOwnerProfileImage = owner.getProfileImage();
        this.postOwnerHandle = post.getOwnerHandle();
        this.isFollow = isFollow;
        this.isLike = post.getLikeUserHandles().contains(loginUser.getHandle());
        this.postImageUrl = post.getImgUrl();
        this.numOfHeart = post.getLikeUserHandles().size();
        this.caption = post.getCaption();
        this.mainComment = new MainComment(randomComment.getCommentUserHandle(), randomComment.getContent());
    }

    public PostDetailResDto(Post post, User owner, User loginUser, Boolean isFollow) {
        this.taggedUserHandles = post.getTaggedUserHandles();
        this.postOwnerProfileImage = owner.getProfileImage();
        this.postOwnerHandle = post.getOwnerHandle();
        this.isFollow = isFollow;
        this.isLike = post.getLikeUserHandles().contains(loginUser.getHandle());
        this.postImageUrl = post.getImgUrl();
        this.numOfHeart = post.getLikeUserHandles().size();
        this.caption = post.getCaption();
    }
}

