package com.apps.pochak.login.controller;

import com.apps.pochak.global.apiPayload.ApiResponse;
import com.apps.pochak.login.dto.request.UserInfoRequest;
import com.apps.pochak.login.jwt.JwtHeaderUtil;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.login.oauth.AppleOAuthService;
import com.apps.pochak.login.oauth.GoogleOAuthService;
import com.apps.pochak.login.oauth.OAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.apps.pochak.global.Constant.HEADER_APPLE_AUTHORIZATION_CODE;
import static com.apps.pochak.global.Constant.HEADER_IDENTITY_TOKEN;
import static com.apps.pochak.global.apiPayload.code.status.SuccessStatus.SUCCESS_LOG_OUT;
import static com.apps.pochak.global.apiPayload.code.status.SuccessStatus.SUCCESS_SIGN_OUT;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final JwtService jwtService;
    private final OAuthService oAuthService;
    private final AppleOAuthService appleOAuthService;
    private final GoogleOAuthService googleOAuthService;

    /**
     * GOOGLE 소셜 로그인 기능
     */
    @GetMapping("/google/login/{accessToken}")
    public ApiResponse<?> googleOAuthRequest(@PathVariable String accessToken) {
        return ApiResponse.onSuccess(googleOAuthService.login(accessToken));
    }

    @PostMapping(value = "/api/v1/user/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<?> signup(UserInfoRequest userInfoRequest) throws IOException {
        return ApiResponse.onSuccess(oAuthService.signup(userInfoRequest));
    }

    /**
     * Token 갱신
     */
    @PostMapping("/api/v1/user/refresh")
    public ApiResponse<?> refresh() {
        return ApiResponse.onSuccess(jwtService.reissueAccessToken());
    }

    /**
     * APPLE 소셜 로그인 기능
     */
    @PostMapping("/apple/login")
    public ApiResponse<?> appleOAuthRequest(@RequestHeader(HEADER_IDENTITY_TOKEN) String idToken,
                                            @RequestHeader(HEADER_APPLE_AUTHORIZATION_CODE) String authorizationCode) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return ApiResponse.onSuccess(appleOAuthService.login(idToken, authorizationCode));
    }

    @GetMapping("/api/v1/user/logout")
    public ApiResponse<?> logout() {
        String accessToken = JwtHeaderUtil.getAccessToken();
        String handle = jwtService.getHandle(accessToken);
        oAuthService.logout(handle);
        return ApiResponse.of(SUCCESS_LOG_OUT,null);
    }

    @DeleteMapping("/api/v1/user/signout")
    public ApiResponse<?> signout() {
        String accessToken = JwtHeaderUtil.getAccessToken();
        String handle = jwtService.getHandle(accessToken);
        oAuthService.signout(handle);
        return ApiResponse.of(SUCCESS_SIGN_OUT, null);
    }
}
