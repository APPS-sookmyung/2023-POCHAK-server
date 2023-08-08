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
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.apps.pochak.common.BaseResponseStatus.EXIST_USER_ID;
import static com.apps.pochak.common.BaseResponseStatus.INVALID_USER_ID;
import static com.apps.pochak.common.Constant.HEADER_AUTHORIZATION;
import static com.apps.pochak.common.Constant.TOKEN_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
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

    public OAuthResponse login(String code) throws JsonProcessingException {
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
    public String getAccessToken(String code) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_REDIRECT_URI);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_BASE_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String accessToken = objectMapper.readValue(responseEntity.getBody(), GoogleTokenResponse.class).getAccessToken();
            log.info(accessToken);
            return accessToken;
        }

        return null;
    }

    /**
     * Get User Info Using Access Token
     */
    public GoogleUserResponse getUserInfo(String accessToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_AUTHORIZATION, TOKEN_PREFIX + accessToken);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(GOOGLE_USER_BASE_URL, HttpMethod.GET, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            GoogleUserResponse userInfo = objectMapper.readValue(responseEntity.getBody(), GoogleUserResponse.class);
            log.info(userInfo.getId());
            return userInfo;
        }
        return null;
    }
}
