package com.apps.pochak.user.controller;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{handle}/exists")
    public BaseResponse<Boolean> checkHandleDuplicate(@PathVariable("handle") String handle) {
        try {
            return new BaseResponse<>(userService.checkHandleDuplicate(handle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
