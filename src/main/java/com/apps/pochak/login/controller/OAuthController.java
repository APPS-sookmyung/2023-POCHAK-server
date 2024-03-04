package com.apps.pochak.login.controller;

import com.apps.pochak.global.api_payload.ApiResponse;
import com.apps.pochak.login.dto.request.MemberInfoRequest;
import com.apps.pochak.login.jwt.JwtHeaderUtil;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.login.oauth.AppleOAuthService;
import com.apps.pochak.login.oauth.GoogleOAuthService;
import com.apps.pochak.login.oauth.OAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.apps.pochak.global.Constant.HEADER_APPLE_AUTHORIZATION_CODE;
import static com.apps.pochak.global.Constant.HEADER_IDENTITY_TOKEN;
import static com.apps.pochak.global.api_payload.code.status.SuccessStatus.SUCCESS_LOG_OUT;
import static com.apps.pochak.global.api_payload.code.status.SuccessStatus.SUCCESS_SIGN_OUT;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final JwtService jwtService;
    private final OAuthService oAuthService;
    private final AppleOAuthService appleOAuthService;
    private final GoogleOAuthService googleOAuthService;

    @GetMapping("/google/login/{accessToken}")
    public ApiResponse<?> googleOAuthRequest(@PathVariable String accessToken) {
        return ApiResponse.onSuccess(googleOAuthService.login(accessToken));
    }

    @PostMapping(value = "/api/v2/member/signup")
    public ApiResponse<?> signup(@ModelAttribute final MemberInfoRequest memberInfoRequest) {
        return ApiResponse.onSuccess(oAuthService.signup(memberInfoRequest));
    }

    @PostMapping("/api/v2/member/refresh")
    public ApiResponse<?> refresh() {
        return ApiResponse.onSuccess(jwtService.reissueAccessToken());
    }

    @PostMapping("/apple/login")
    public ApiResponse<?> appleOAuthRequest(@RequestHeader(HEADER_IDENTITY_TOKEN) String idToken,
                                            @RequestHeader(HEADER_APPLE_AUTHORIZATION_CODE) String authorizationCode) throws NoSuchAlgorithmException, InvalidKeySpecException, JsonProcessingException {
        return ApiResponse.onSuccess(appleOAuthService.login(idToken, authorizationCode));
    }

    @GetMapping("/api/v2/member/logout")
    public ApiResponse<?> logout() {
        String accessToken = JwtHeaderUtil.getAccessToken();
        String handle = jwtService.getHandle(accessToken);
        oAuthService.logout(handle);
        return ApiResponse.of(SUCCESS_LOG_OUT, null);
    }

    @DeleteMapping("/api/v2/member/signout")
    public ApiResponse<?> signout() {
        String accessToken = JwtHeaderUtil.getAccessToken();
        String handle = jwtService.getHandle(accessToken);
        oAuthService.signout(handle);
        return ApiResponse.of(SUCCESS_SIGN_OUT, null);
    }
}
