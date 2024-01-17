package com.apps.pochak.member.controller;

import com.apps.pochak.global.apiPayload.ApiResponse;
import com.apps.pochak.member.dto.response.ProfileResponse;
import com.apps.pochak.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{handle}")
    public ApiResponse<ProfileResponse> getProfileDetail(@PathVariable final String handle,
                                                         @PageableDefault(12) final Pageable pageable) {
        return ApiResponse.onSuccess(memberService.getProfileDetail(handle, pageable));
    }
}
