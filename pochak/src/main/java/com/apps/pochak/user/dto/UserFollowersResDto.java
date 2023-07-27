package com.apps.pochak.user.dto;

import com.apps.pochak.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 유저의 기본 DTO
 */
public class UserFollowersResDto {

    private int numOfFollowers;
    private List<UserEachDto> followers = new ArrayList<>();

    @Builder
    public UserFollowersResDto(List<User> users) {
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class UserEachDto {
        private String profileImgUrl;
        private String handle;
        private String user;
    }
}