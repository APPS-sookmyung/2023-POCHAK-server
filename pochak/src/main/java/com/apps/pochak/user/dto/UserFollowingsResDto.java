package com.apps.pochak.user.dto;

import com.apps.pochak.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.apps.pochak.user.dto.UserFollowersResDto.UserEachDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Following List Response DTO
 */
public class UserFollowingsResDto {
    private int numOfFollowings;
    private List<UserEachDto> followings = new ArrayList<>();

    public UserFollowingsResDto(List<User> followings) {
        List<UserEachDto> dtos = followings.stream().map(
                user -> new UserEachDto(user)).collect(Collectors.toList());
        this.followings = dtos;
        this.numOfFollowings = followings.size();
    }
}
