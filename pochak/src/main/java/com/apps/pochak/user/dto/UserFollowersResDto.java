package com.apps.pochak.user.dto;

import com.apps.pochak.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * Follower List Response DTO
 */
public class UserFollowersResDto {

    private int numOfFollowers;
    private List<UserEachDto> followers = new ArrayList<>();

    @Builder
    public UserFollowersResDto(List<User> users) {
        List<UserEachDto> followersDto = users.stream().map(
                user -> new UserEachDto(user)).collect(Collectors.toList());
        this.followers = followersDto;
        this.numOfFollowers = followersDto.size();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class UserEachDto {
        private String profileImgUrl;
        private String handle; //id
        private String userName;

        public UserEachDto(User user) {
            this.profileImgUrl = user.getProfileImage();
            this.handle = user.getHandle();
            this.userName = user.getName();
        }
    }
}