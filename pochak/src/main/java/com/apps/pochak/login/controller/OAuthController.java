package com.apps.pochak.login.controller;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.login.dto.UserInfoRequest;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.login.oauth.GoogleOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<?> googleOAuthRequest(@RequestParam String code) throws BaseException {
        return new BaseResponse<>(googleOAuthService.login(code));
    }

    @ResponseBody
    @PostMapping("/api/v1/user/signup")
    public BaseResponse<?> signup(@RequestBody UserInfoRequest userInfoRequest) {
        try {
            return new BaseResponse<>(googleOAuthService.signup(userInfoRequest));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * Token 갱신
     */
    @ResponseBody
    @PostMapping("/api/v1/user/refresh")
    public BaseResponse<?> refresh() {
        try {
            return new BaseResponse<>(jwtService.reissueAccessToken());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
