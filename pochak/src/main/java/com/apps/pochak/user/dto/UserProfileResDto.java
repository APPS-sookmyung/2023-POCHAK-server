package com.apps.pochak.user.dto;

import com.apps.pochak.post.domain.Post;
import com.apps.pochak.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserProfileResDto {
    private String handle;
    private String userProfileImg;
    private String userName;
    private String message;
    private int totalPostNum;
    private int postCount;
    private int followerCount;
    private int followingCount;

    // 남의 프로필 조회할 경우 사용
    private boolean isFollow;

    private List<ProfilePostDto> taggedPosts;

    @Builder
    public UserProfileResDto(User user, User loginUser, List<Post> taggedPosts) {
        this.handle = user.getHandle();
        this.userProfileImg = user.getProfileImage();
        this.userName = user.getName();
        this.message = user.getMessage();
        this.totalPostNum = user.getTaggedPostPKs().size();
        this.followerCount = user.getFollowerUserHandles().size();
        this.followingCount = user.getFollowingUserHandles().size();
        if (!user.getHandle().equals(loginUser.getHandle())) {
            // 유저와 로그인한 유저가 다를 때만 팔로우 상태 제공
            this.isFollow = user.getFollowerUserHandles().contains(loginUser.getHandle());
        }
        this.taggedPosts = taggedPosts.stream().map(post -> {
            return new ProfilePostDto(post);
        }).collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    public static class ProfilePostDto {
        private String postPK;
        private String postImg;

        public ProfilePostDto(Post post) {
            this.postPK = post.getPostPK();
            this.postImg = post.getImgUrl();
        }
    }
}
