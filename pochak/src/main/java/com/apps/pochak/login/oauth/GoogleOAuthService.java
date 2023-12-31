package com.apps.pochak.login.oauth;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.login.dto.GoogleTokenResponse;
import com.apps.pochak.login.dto.GoogleUserResponse;
import com.apps.pochak.login.dto.OAuthResponse;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.apps.pochak.common.BaseResponseStatus.*;

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

    public OAuthResponse login(String accessToken) throws BaseException {
        GoogleUserResponse userResponse = getUserInfo(accessToken);

        User user = userRepository.findUserWithSocialId(userResponse.getId()).orElse(null);

        if (user == null) {
            return OAuthResponse.builder()
                    .isNewMember(true)
                    .socialType("google")
                    .id(userResponse.getId())
                    .name(userResponse.getName())
                    .email(userResponse.getEmail())
                    .build();
        }

        String appRefreshToken = jwtService.createRefreshToken();
        String appAccessToken = jwtService.createAccessToken(user.getHandle());

        user.updateRefreshToken(appRefreshToken);
        userRepository.saveUser(user);
        return OAuthResponse.builder()
                .isNewMember(false)
                .accessToken(appAccessToken)
                .refreshToken(appRefreshToken)
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
                        .queryParam("client_secret", GOOGLE_CLIENT_SECRET)
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
                .orElseThrow(() -> new BaseException(INVALID_OAUTH_TOKEN));

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
