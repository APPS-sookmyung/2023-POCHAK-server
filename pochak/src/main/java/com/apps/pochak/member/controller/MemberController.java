package com.apps.pochak.member.controller;

import com.apps.pochak.follow.service.FollowService;
import com.apps.pochak.global.apiPayload.ApiResponse;
import com.apps.pochak.member.service.MemberService;
import com.apps.pochak.post.dto.PostElements;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/members")
public class MemberController {
    private final MemberService memberService;
    private final FollowService followService;

    @GetMapping("/{handle}")
    public ApiResponse<?> getProfileDetail(
            @PathVariable final String handle,
            @PageableDefault(12) final Pageable pageable
    ) {
        if (pageable.getPageSize() == 0)
            return ApiResponse.onSuccess(memberService.getProfileDetail(handle, pageable));
        else
            return ApiResponse.onSuccess(memberService.getTaggedPosts(handle, pageable));
    }

    @GetMapping("/{handle}/upload")
    public ApiResponse<PostElements> getUploadPosts(
            @PathVariable final String handle,
            @PageableDefault(12) final Pageable pageable
    ) {
        return ApiResponse.onSuccess(memberService.getUploadPosts(handle, pageable));
    }

    @PostMapping("/{handle}/follow")
    public ApiResponse<Void> followMember(
            @PathVariable final String handle
    ) {
        return followService.follow(handle);
    }

    @DeleteMapping("/{handle}/follower")
    public ApiResponse<Void> deleteFollower(
            @PathVariable final String handle,
            @RequestParam("followerHandle") final String followerHandle
    ) {
        return followService.deleteFollower(handle, followerHandle);
    }
}
