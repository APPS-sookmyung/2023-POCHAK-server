package com.apps.pochak.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class LikedUsersResDto {
    private List<LikedUser> likedUsers;

    public LikedUsersResDto(List<LikedUser> likedUsers) {
        this.likedUsers = likedUsers;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikedUser {
        private String userHandle;
        private String profileImage;
        private String name;
        private Boolean follow;
    }
}
