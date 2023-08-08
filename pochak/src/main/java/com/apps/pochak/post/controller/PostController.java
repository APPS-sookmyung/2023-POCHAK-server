package com.apps.pochak.post.controller;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.dto.PostResDto;
import com.apps.pochak.post.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    // post 저장 api
    @PostMapping("/api/v1/user/postupload")
    public Post savePost(@RequestBody Post post){
        return postService.savePost(post);
    }

    // post detail 가져오는 api
    @GetMapping("/api/v1/postId")
    public BaseResponse<PostResDto> findPostDetailByPostId(@RequestParam("postPK") String postPK){
        try{
            return new BaseResponse<>(postService.getPostDetail(postPK));
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    // 부모 comment 조회
//    @GetMapping("/api/v1/postId/parentcomments")
//    public BaseResponse<PostResDto> findParentCommentsByPostId(@RequestParam("userPK") String userPK){
//        try{
//            return new BaseResponse<>(postService.getParentComments(userPK));
//        }catch (BaseException e){
//            return new BaseResponse<>(e.getStatus());
//        }
//    }





}
