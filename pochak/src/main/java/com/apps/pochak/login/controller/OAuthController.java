package com.apps.pochak.login.controller;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.login.dto.UserInfoRequest;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.login.oauth.AppleOAuthService;
import com.apps.pochak.login.oauth.GoogleOAuthService;
import com.apps.pochak.login.oauth.OAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.apps.pochak.common.Constant.HEADER_APPLE_AUTHORIZATION_CODE;
import static com.apps.pochak.common.Constant.HEADER_IDENTITY_TOKEN;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final JwtService jwtService;
    private final OAuthService oAuthService;
    private final AppleOAuthService appleOAuthService;
    private final GoogleOAuthService googleOAuthService;

    /**
     * GOOGLE 소셜 로그인 기능
     * https://localhost:8050/login/oauth2/code/google?code=코드정보
     */
    @ResponseBody
    @GetMapping("/google/login")
    public BaseResponse<?> googleOAuthRequest(@RequestParam String code) throws BaseException {
        return new BaseResponse<>(googleOAuthService.login(code));
    }

    @ResponseBody
    @PostMapping("/api/v1/user/signup")
    public BaseResponse<?> signup(@RequestBody UserInfoRequest userInfoRequest) {
        return new BaseResponse<>(oAuthService.signup(userInfoRequest));
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

    /**
     * APPLE 소셜 로그인 기능
     */
    @ResponseBody
    @PostMapping("/apple/login")
    public BaseResponse<?> appleOAuthRequest(@RequestHeader(HEADER_IDENTITY_TOKEN) String idToken,
                                             @RequestHeader(HEADER_APPLE_AUTHORIZATION_CODE) String authorizationCode) {
        try {
            return new BaseResponse<>(appleOAuthService.login(idToken, authorizationCode));
        } catch (BaseException | JsonProcessingException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            return new BaseResponse<>(e.getMessage());
        }
    }

    /**
     * APPLE 연동 해제
     */
    @ResponseBody
    @PostMapping("/apple/revoke")
    public BaseResponse<?> appleLoginRevoke() {
        try {
            return new BaseResponse<>(appleOAuthService.revoke());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
