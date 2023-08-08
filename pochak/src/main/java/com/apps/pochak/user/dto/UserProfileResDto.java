package com.apps.pochak.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private List<ProfilePostDto> pochakedPosts;

    @Data
    @NoArgsConstructor
    public static class ProfilePostDto {
        private String postPK;
        private String postImg;
    }
}
