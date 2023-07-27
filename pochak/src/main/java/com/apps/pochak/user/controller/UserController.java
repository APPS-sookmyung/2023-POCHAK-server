package com.apps.pochak.user.controller;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.dto.UserFollowersResDto;
import com.apps.pochak.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/v1/user")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/api/v1/profile/follower")
    public BaseResponse<UserFollowersResDto> findUserByUserPK(@RequestParam("userPK") String userPK) {
        try {
            return new BaseResponse<>(userService.getUserFollowers(userPK));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
