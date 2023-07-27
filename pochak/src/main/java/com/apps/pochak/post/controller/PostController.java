package com.apps.pochak.post.controller;

import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // post 저장 api
    @PostMapping("/api/v1/user/postupload")
    public Post savePost(@RequestBody Post post){
        return postService.savePost(post);
    }






}
