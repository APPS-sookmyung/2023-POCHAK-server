package com.apps.pochak.login.controller;

import com.apps.pochak.common.ApiResponse;
import com.apps.pochak.login.dto.OAuthResponse;
import com.apps.pochak.login.service.GoogleOAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final GoogleOAuthService googleOAuthService;
    /**
     * GOOGLE 소셜 로그인 기능
     * https://localhost:8080/login/oauth2/code/google?code=코드정보
     */
    @ResponseBody
    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<OAuthResponse> googleOAuthRequest(@RequestParam String code) throws JsonProcessingException {
        return ApiResponse.success(googleOAuthService.login(code));
    }
}
