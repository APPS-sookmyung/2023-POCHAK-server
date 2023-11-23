package com.apps.pochak.post.controller;

import com.apps.pochak.comment.dto.CommentDeleteRequestDto;
import com.apps.pochak.comment.dto.CommentResDto;
import com.apps.pochak.comment.dto.CommentUploadRequestDto;
import com.apps.pochak.comment.service.CommentService;
import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.login.jwt.JwtHeaderUtil;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.post.dto.PostDetailResDto;
import com.apps.pochak.post.dto.PostUploadRequestDto;
import com.apps.pochak.post.dto.PostUploadResDto;
import com.apps.pochak.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.apps.pochak.common.BaseResponseStatus.NULL_COMMENTS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final JwtService jwtService;

    @PostMapping(value="", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<PostUploadResDto> savePost(PostUploadRequestDto requestDto) {
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            return new BaseResponse<>(postService.savePost(requestDto, loginUserHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{postPK}")
    public BaseResponse<PostDetailResDto> findPostDetailByPostId(@PathVariable("postPK") String postPK) {
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            PostDetailResDto postDetail = postService.getPostDetail(postPK, loginUserHandle);
            if (postDetail.getMainComment() == null) {
                return new BaseResponse<>(postDetail, NULL_COMMENTS);
            }
            return new BaseResponse<>(postDetail);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    // postPK를 포함해서 요청 받음
    // SK가 COMMENT#PARENT#(생성날짜)
    @PostMapping("/{postPK}/comment")
    public BaseResponse<CommentResDto> commentUpload(@PathVariable("postPK") String postPK,
                                                     @RequestBody CommentUploadRequestDto requestDto) {
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            return new BaseResponse<>(commentService.commentUpload(
                    postPK,
                    requestDto,
                    loginUserHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PostMapping("/{postPK}/like")
    public BaseResponse likePost(@PathVariable("postPK") String postPK) {
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            return new BaseResponse<>(postService.likePost(postPK, loginUserHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/{postPK}/like")
    public BaseResponse getUsersLikedPost(@PathVariable("postPK") String postPK) {
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            return new BaseResponse<>(postService.getUsersLikedPost(postPK, loginUserHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{postPK}")
    public BaseResponse deletePost(@PathVariable("postPK") String postPK) {
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            return new BaseResponse<>(postService.deletePost(postPK, loginUserHandle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @DeleteMapping("/{postPK}/comment")
    public BaseResponse deleteComment(@PathVariable("postPK") String postPK,
                                      @RequestBody CommentDeleteRequestDto requestDto) {
        try {
            // login
            String accessToken = JwtHeaderUtil.getAccessToken();
            String loginUserHandle = jwtService.getHandle(accessToken);

            return new BaseResponse<>(commentService.deleteComment(postPK, loginUserHandle, requestDto));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

