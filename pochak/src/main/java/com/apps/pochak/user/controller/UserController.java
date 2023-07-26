package com.apps.pochak.user.controller;

import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // test api
    @PostMapping("/api/v1/user")
    public User saveCustomer(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // test api
    @GetMapping("/api/v1/user")
    public User findUserByUserPK(@RequestParam("userPK") String userPK) {
        return userService.findUserByUserPK(userPK);
    }
}
