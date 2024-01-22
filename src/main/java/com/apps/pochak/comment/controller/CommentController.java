package com.apps.pochak.comment.controller;

import com.apps.pochak.comment.dto.request.CommentUploadRequest;
import com.apps.pochak.comment.dto.response.CommentElements;
import com.apps.pochak.comment.dto.response.ParentCommentElement;
import com.apps.pochak.comment.service.CommentService;
import com.apps.pochak.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.apps.pochak.comment.service.CommentService.DEFAULT_PAGING_SIZE;
import static com.apps.pochak.global.apiPayload.code.status.SuccessStatus.SUCCESS_UPLOAD_COMMENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("")
    public ApiResponse<CommentElements> getComments(
            @PathVariable("postId") final Long postId,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(commentService.getComments(postId, pageable));
    }

    @GetMapping("{parentCommentId}")
    public ApiResponse<ParentCommentElement> getChildComments(
            @PathVariable("postId") final Long postId,
            @PathVariable("parentCommentId") final Long parentCommentId,
            @PageableDefault(DEFAULT_PAGING_SIZE) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(commentService.getChildCommentsByParentCommentId(postId, parentCommentId, pageable));
    }

    @PostMapping("")
    public ApiResponse<Void> getChildComments(
            @PathVariable("postId") final Long postId,
            @RequestBody @Valid final CommentUploadRequest request
    ) {
        commentService.saveComment(postId, request);
        return ApiResponse.of(SUCCESS_UPLOAD_COMMENT);
    }
}
