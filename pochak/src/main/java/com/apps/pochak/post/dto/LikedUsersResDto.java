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
    // 좋아요 누른 유저들의 정보 반환
    // handle
    // 프사
    // 이름
    // 내가 팔로우 하는지 여부
    private List<likedUser> likedUsers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class likedUser{
        private String userHandle;
        private String profileImage;
        private String name;
        private boolean follow;

    }


    public LikedUsersResDto(List<likedUser> likedUsers){
        this.likedUsers=likedUsers;
    }
}
