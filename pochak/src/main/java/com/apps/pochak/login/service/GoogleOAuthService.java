package com.apps.pochak.login.service;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.login.dto.GoogleTokenResponse;
import com.apps.pochak.login.dto.GoogleUserResponse;
import com.apps.pochak.login.dto.OAuthResponse;
import com.apps.pochak.login.dto.UserInfoRequest;
import com.apps.pochak.user.domain.SocialType;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.apps.pochak.common.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {
    private final UserRepository userRepository;
    private final WebClient webClient;
    private final JwtService jwtService;

    @Value("${oauth2.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${oauth2.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;
    @Value("${oauth2.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${oauth2.google.base-url}")
    private String GOOGLE_BASE_URL;
    @Value("${oauth2.google.user-base-url}")
    private String GOOGLE_USER_BASE_URL;

    public OAuthResponse login(String code) throws BaseException {
        String accessToken = getAccessToken(code);
        GoogleUserResponse userResponse = getUserInfo(accessToken);

        User user = userRepository.findUserWithSocialId(userResponse.getId()).orElse(null);

        if (user == null) {
            return OAuthResponse.builder()
                    .isNewMember(true)
                    .id(userResponse.getId())
                    .name(userResponse.getName())
                    .socialType(SocialType.GOOGLE.getCode())
                    .email(userResponse.getEmail())
                    .build();
        }

        return OAuthResponse.builder()
                .isNewMember(false)
                .id(user.getHandle())
                .build();
    }

    public OAuthResponse signup(UserInfoRequest userInfoRequest) throws BaseException {
        userRepository.findUserWithSocialId(userInfoRequest.getSocialId())
                .ifPresent(i -> new BaseException(EXIST_USER_ID));

        String refreshToken = jwtService.createRefreshToken();
        String accessToken = jwtService.createAccessToken(userInfoRequest.getHandle());

        User user = User.signupUser()
                .refreshToken(refreshToken)
                .name(userInfoRequest.getName())
                .email(userInfoRequest.getEmail())
                .handle(userInfoRequest.getHandle())
                .message(userInfoRequest.getMessage())
                .socialId(userInfoRequest.getSocialId())
                .profileImage(userInfoRequest.getProfileImage())
                .socialType(SocialType.of(userInfoRequest.getSocialType()))
                .build();

        user.updateRefreshToken(refreshToken);
        userRepository.saveUser(user);
        return OAuthResponse.builder()
                .isNewMember(false)
                .id(user.getHandle())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Get Access Token
     */
    public String getAccessToken(String code) throws BaseException {
        GoogleTokenResponse googleTokenResponse = webClient.post()
                .uri(GOOGLE_BASE_URL, uriBuilder -> uriBuilder
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", GOOGLE_CLIENT_ID)
                        .queryParam("client_secret",  GOOGLE_CLIENT_SECRET)
                        .queryParam("redirect_uri", GOOGLE_REDIRECT_URI)
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RuntimeException("Social Access Token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(GoogleTokenResponse.class)
                .flux()
                .toStream()
                .findFirst()
                .orElseThrow(() -> new BaseException(INVALID_ACCESS_TOKEN));

        log.info(googleTokenResponse.getAccessToken());
        return googleTokenResponse.getAccessToken();
    }

    /**
     * Get User Info Using Access Token
     */
    public GoogleUserResponse getUserInfo(String accessToken) throws BaseException {
        GoogleUserResponse googleUserResponse = webClient.get()
                .uri(GOOGLE_USER_BASE_URL, uriBuilder -> uriBuilder.queryParam("access_token", accessToken).build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RuntimeException("Social Access Token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(GoogleUserResponse.class)
                .flux()
                .toStream()
                .findFirst()
                .orElseThrow(() -> new BaseException(INVALID_USER_INFO));

        return googleUserResponse;
    }
}
