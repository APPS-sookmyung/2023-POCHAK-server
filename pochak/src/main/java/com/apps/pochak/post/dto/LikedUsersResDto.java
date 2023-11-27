package com.apps.pochak.post.dto;

import com.apps.pochak.post.domain.Post;
import com.apps.pochak.user.domain.User;
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

        public LikedUser(User user, User loginUser) {
            this.userHandle = user.getHandle();
            this.profileImage = user.getProfileImage();
            this.name = user.getName();
            this.follow = loginUser.getFollowingUserHandles().contains(userHandle);
        }
    }
}
