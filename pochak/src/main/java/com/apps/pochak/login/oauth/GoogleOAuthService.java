package com.apps.pochak.login.oauth;

import com.apps.pochak.global.apiPayload.exception.handler.GoogleOAuthException;
import com.apps.pochak.login.dto.response.GoogleTokenResponse;
import com.apps.pochak.login.dto.response.GoogleMemberResponse;
import com.apps.pochak.login.dto.response.OAuthMemberResponse;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.member.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.INVALID_OAUTH_TOKEN;
import static com.apps.pochak.global.apiPayload.code.status.ErrorStatus.INVALID_USER_INFO;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {
    private final MemberRepository memberRepository;
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

    @Transactional
    public OAuthMemberResponse login(String accessToken) {
        String token = getAccessToken(accessToken);
        GoogleMemberResponse userResponse = getUserInfo(token);

        Member member = memberRepository.findMemberBySocialId(userResponse.getId()).orElse(null);

        if (member == null) {
            return OAuthMemberResponse.builder()
                    .socialId(userResponse.getId())
                    .name(userResponse.getName())
                    .email(userResponse.getEmail())
                    .socialType("google")
                    .isNewMember(true)
                    .build();
        }

        String appRefreshToken = jwtService.createRefreshToken();
        String appAccessToken = jwtService.createAccessToken(member.getHandle());

        member.updateRefreshToken(appRefreshToken);
        memberRepository.save(member);
        return OAuthMemberResponse.builder()
                .socialId(userResponse.getId())
                .name(userResponse.getName())
                .email(userResponse.getEmail())
                .socialType("google")
                .accessToken(appAccessToken)
                .refreshToken(appRefreshToken)
                .isNewMember(false)
                .build();
    }

    /**
     * Get Access Token
     */
    public String getAccessToken(String code) {
        GoogleTokenResponse googleTokenResponse = webClient.post()
                .uri(GOOGLE_BASE_URL, uriBuilder -> uriBuilder
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", GOOGLE_CLIENT_ID)
                        .queryParam("client_secret", GOOGLE_CLIENT_SECRET)
                        .queryParam("redirect_uri", GOOGLE_REDIRECT_URI)
                        .queryParam("code", code)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new RuntimeException("Social Access Token is unauthorized")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(GoogleTokenResponse.class)
                .flux()
                .toStream()
                .findFirst()
                .orElseThrow(() -> new GoogleOAuthException(INVALID_OAUTH_TOKEN));

        return googleTokenResponse.getAccessToken();
    }

    /**
     * Get User Info Using Access Token
     */
    public GoogleMemberResponse getUserInfo(String accessToken) {
        GoogleMemberResponse googleMemberResponse = webClient.get()
                .uri(GOOGLE_USER_BASE_URL, uriBuilder -> uriBuilder.queryParam("access_token", accessToken).build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new RuntimeException("Social Access Token is unauthorized")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(GoogleMemberResponse.class)
                .flux()
                .toStream()
                .findFirst()
                .orElseThrow(() -> new GoogleOAuthException(INVALID_USER_INFO));

        return googleMemberResponse;
    }
}
