package com.apps.pochak.likes.controller;

import com.apps.pochak.likes.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likesService;
}
