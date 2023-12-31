package com.apps.pochak.user.dto;

import com.apps.pochak.publish.domain.Publish;
import com.apps.pochak.tag.domain.Tag;
import com.apps.pochak.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserProfileResDto {
    private String handle;
    private String userProfileImg;
    private String userName;
    private String message;
    private int totalPostNum;
    private int followerCount;
    private int followingCount;

    // 남의 프로필 조회할 경우 사용
    private Boolean isFollow;

    private List<ProfilePostDto> taggedPosts = new ArrayList<>();
    private Map<String, String> exclusiveStartKey;

    @Builder
    public UserProfileResDto(User user, User loginUser, List<Tag> tags,
                             Boolean isFollow, Map<String, String> exclusiveStartKey) {
        this.handle = user.getHandle();
        this.userProfileImg = user.getProfileImage();
        this.userName = user.getName();
        this.message = user.getMessage();
        this.totalPostNum = tags.size();
        this.followerCount = user.getFollowerCount();
        this.followingCount = user.getFollowingCount();

        if (!user.getHandle().equals(loginUser.getHandle())) {
            // 유저와 로그인한 유저가 다를 때만 팔로우 상태 제공
            this.isFollow = isFollow;
        }

        this.taggedPosts = tags.stream().map(
                tag -> new ProfilePostDto(tag)
        ).collect(Collectors.toList());
        this.exclusiveStartKey = exclusiveStartKey;
    }

    @Data
    @NoArgsConstructor
    public static class ProfilePostDto {
        private String postPK;
        private String postImg;

        public ProfilePostDto(Tag tag) {
            this.postPK = tag.getPostPK();
            this.postImg = tag.getPostImg();
        }

        public ProfilePostDto(Publish publish) {
            this.postPK = publish.getPostPK();
            this.postImg = publish.getPostImg();
        }
    }
}
