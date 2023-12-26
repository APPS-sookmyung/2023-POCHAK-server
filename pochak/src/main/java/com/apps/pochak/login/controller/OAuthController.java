package com.apps.pochak.login.controller;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.common.BaseResponse;
import com.apps.pochak.login.dto.UserInfoRequest;
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
     */
    @ResponseBody
    @GetMapping("/google/login/{accessToken}")
    public BaseResponse<?> googleOAuthRequest(@PathVariable("accessToken") String accessToken) throws BaseException {
        return new BaseResponse<>(googleOAuthService.login(accessToken));
    }

    @ResponseBody
    @PostMapping(value="/api/v1/user/signup", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<?> signup(UserInfoRequest userInfoRequest) throws IOException {
        try {
            return new BaseResponse<>(oAuthService.signup(userInfoRequest));
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

    @ResponseBody
    @GetMapping("/api/v1/user/logout")
    public BaseResponse<?> logout() {
        try {
            String accessToken = JwtHeaderUtil.getAccessToken();
            String handle = jwtService.getHandle(accessToken);
            return new BaseResponse<>(oAuthService.logout(handle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @DeleteMapping ("/api/v1/user/signout")
    public BaseResponse<?> signout() {
        try {
            String accessToken = JwtHeaderUtil.getAccessToken();
            String handle = jwtService.getHandle(accessToken);
            return new BaseResponse<>(oAuthService.signout(handle));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
