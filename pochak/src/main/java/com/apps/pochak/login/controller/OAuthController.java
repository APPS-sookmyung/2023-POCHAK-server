package com.apps.pochak.login.controller;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.login.dto.UserInfoRequest;
import com.apps.pochak.login.service.GoogleOAuthService;
import com.apps.pochak.login.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final GoogleOAuthService googleOAuthService;
    private final JwtService jwtService;

    /**
     * GOOGLE 소셜 로그인 기능
     * https://localhost:8080/login/oauth2/code/google?code=코드정보
     */
    @ResponseBody
    @GetMapping("/login/oauth2/code/google")
    public BaseResponse<?> googleOAuthRequest(@RequestParam String code) throws JsonProcessingException, BaseException {
        return new BaseResponse<>(googleOAuthService.login(code));
    }

    @ResponseBody
    @PostMapping("/signup")
    public BaseResponse<?> signup(@RequestBody UserInfoRequest userInfoRequest) {
        try {
            return new BaseResponse<>(googleOAuthService.signup(userInfoRequest));
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * Token 갱신
     */
    @GetMapping("/refresh")
    public BaseResponse<?> refresh(HttpServletRequest request) throws BaseException {
        try {
            return new BaseResponse<>(jwtService.reissueToken(request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}