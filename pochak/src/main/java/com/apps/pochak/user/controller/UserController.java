package com.apps.pochak.user.controller;

import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/v1/user")
    public User saveCustomer(@RequestBody User user) {
        return userService.saveUser(user);
    }
}
