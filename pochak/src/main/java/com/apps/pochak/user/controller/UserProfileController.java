package com.apps.pochak.user.controller;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.user.dto.UserFollowersResDto;
import com.apps.pochak.user.dto.UserFollowingsResDto;
import com.apps.pochak.user.dto.UserUpdateRequestDto;
import com.apps.pochak.user.dto.UserUpdateResDto;
import com.apps.pochak.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.apps.pochak.common.BaseResponseStatus.INVALID_UPDATE_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/profile")
public class UserProfileController {
    private final UserService userService;

    // TODO: 유저 로그인 로직 추가
    @GetMapping("/{handle}/follower")
    public BaseResponse<UserFollowersResDto> findFollowers(@PathVariable("handle") String userHandle) {
        try {
            return new BaseResponse<>(userService.getUserFollowers(userHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // TODO: 유저 로그인 로직 추가
    @GetMapping("/{handle}/following")
    public BaseResponse<UserFollowingsResDto> findFollowings(@PathVariable("handle") String userHandle) {
        try {
            return new BaseResponse<>(userService.getUserFollowings(userHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // TODO: loginUserHandle -> 추후 로그인 로직 필요
    @PutMapping("/{handle}")
    public BaseResponse<UserUpdateResDto> updateUserProfile(@PathVariable("handle") String updatedUserHandle,
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
