package com.apps.pochak.post.controller;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.post.dto.PostResDto;
import com.apps.pochak.post.dto.PostUploadRequestDto;
import com.apps.pochak.post.dto.PostUploadResDto;
import com.apps.pochak.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    // TODO: param으로 받은 로그인 정보 추후 수정 필요
    // post 저장 api
    @PostMapping("")
    public PostUploadResDto savePost(@RequestBody PostUploadRequestDto requestDto,
                                     @RequestParam("loginUser") String loginUserHandle) {
        try {
            return new BaseResponse<>(postService.savePost(requestDto, loginUserHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // post detail 가져오는 api
    @GetMapping("/{postPK}")
    public BaseResponse<PostResDto> findPostDetailByPostId(@PathVariable("postPK") String postPK) {
        try {
            return new BaseResponse<>(postService.getPostDetail(postPK));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
