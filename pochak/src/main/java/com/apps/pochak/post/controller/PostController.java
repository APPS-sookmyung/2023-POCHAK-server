package com.apps.pochak.post.controller;

import com.apps.pochak.global.apiPayload.ApiResponse;
import com.apps.pochak.global.s3.ValidFile;
import com.apps.pochak.post.dto.request.PostUploadRequest;
import com.apps.pochak.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/posts")
public class PostController {
    private final PostService postService;

    @PostMapping(value = "", consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<Void> uploadPost(
            @RequestPart(value = "postImage")
            @ValidFile(message = "게시물 이미지는 필수로 전달해야 합니다.") final MultipartFile postImage,
            @RequestPart("request") @Valid final PostUploadRequest request
    ) {
        postService.savePost(postImage, request);
        return ApiResponse.onSuccess(null);
    }
}
