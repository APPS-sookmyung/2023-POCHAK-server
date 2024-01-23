package com.apps.pochak.comment.controller;

import com.apps.pochak.comment.dto.response.CommentElements;
import com.apps.pochak.comment.dto.response.ParentCommentElement;
import com.apps.pochak.comment.service.CommentService;
import com.apps.pochak.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.apps.pochak.comment.service.CommentService.DEFAULT_PAGING_SIZE;

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
}
