package com.apps.pochak.user.controller;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.common.BaseResponseStatus;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.dto.UserFollowersResDto;
import com.apps.pochak.user.dto.UserUpdateRequestDto;
import com.apps.pochak.user.dto.UserUpdateResDto;
import com.apps.pochak.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.apps.pochak.common.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/profile/{handle}/follower")
    public BaseResponse<UserFollowersResDto> findUserByUserPK(@PathVariable("handle") String userHandle) {
        try {
            return new BaseResponse<>(userService.getUserFollowers(userHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // TODO: loginUserHandle -> 추후 로그인 로직 필요
    @PutMapping("/profile/{handle}/")
    public BaseResponse<UserUpdateResDto> updateUser(@PathVariable("handle") String updatedUserHandle,
                                                     @RequestParam("loginUser") String loginUserHandle,
                                                     @RequestBody UserUpdateRequestDto requestDto) {
        try {
            if (!updatedUserHandle.equals(loginUserHandle)) throw new BaseException(INVALID_UPDATE_REQUEST);
            return new BaseResponse<>(userService.updateUserProfile(updatedUserHandle, requestDto));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
