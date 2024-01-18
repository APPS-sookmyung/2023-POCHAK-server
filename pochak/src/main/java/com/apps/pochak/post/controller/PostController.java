package com.apps.pochak.post.controller;

import com.apps.pochak.global.apiPayload.ApiResponse;
import com.apps.pochak.post.dto.request.PostUploadRequest;
import com.apps.pochak.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/posts")
public class PostController {
    private final PostService postService;

    @PostMapping(value = "", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<Void> uploadPost(@Valid PostUploadRequest request) {
        postService.savePost(request);
        return ApiResponse.onSuccess(null);
    }
}
