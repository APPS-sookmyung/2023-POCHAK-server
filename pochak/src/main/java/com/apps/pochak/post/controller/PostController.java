package com.apps.pochak.post.controller;

import com.apps.pochak.global.apiPayload.ApiResponse;
import com.apps.pochak.likes.service.LikeService;
import com.apps.pochak.post.dto.response.PostDetailResponse;
import com.apps.pochak.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/posts")
public class PostController {
    private final PostService postService;
    private final LikeService likeService;

    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponse> getPostDetail(
            @PathVariable final Long postId
    ) {
        return ApiResponse.onSuccess(postService.getPostDetail(postId));
    }
}
