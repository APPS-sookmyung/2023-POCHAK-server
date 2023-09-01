package com.apps.pochak.login.oauth;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.login.dto.*;
import com.apps.pochak.login.jwt.JwtHeaderUtil;
import com.apps.pochak.login.jwt.JwtService;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static com.apps.pochak.common.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppleOAuthService {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${oauth2.apple.key-id}")
    private String KEY_ID;
    @Value("${oauth2.apple.team-id}")
    private String TEAM_ID;
    @Value("${oauth2.apple.client-id}")
    private String CLIENT_ID;
    @Value("${oauth2.apple.base-url}")
    private String PUBLIC_KEY_URL;
    @Value("${oauth2.apple.key-id-path}")
    private String KEY_ID_PATH;

    public OAuthResponse login(String idToken, String authorizationCode) throws BaseException, JsonProcessingException, NoSuchAlgorithmException, InvalidKeySpecException {
        Map<String, String> idTokenHeaderMap = getHeaderFromIdToken(idToken);
        Claims claims = verifyIdToken(idTokenHeaderMap.get("kid"), idTokenHeaderMap.get("alg"), idToken);

        String sub = String.valueOf(claims.get("sub"));
        String email = String.valueOf(claims.get("email"));

        User user = userRepository.findUserWithSocialId(sub).orElse(null);

        if (user == null) {
            String appleRefreshToken = getAppleRefreshToken(authorizationCode);
            return OAuthResponse.builder()
                    .id(sub)
                    .email(email)
                    .isNewMember(true)
                    .socialType("apple")
                    .refreshToken(appleRefreshToken)
                    .build();
        }

        String appRefreshToken = jwtService.createRefreshToken();
        String appAccessToken = jwtService.createAccessToken(user.getHandle());

        user.updateRefreshToken(appRefreshToken);
        userRepository.updateUser(user);
        return OAuthResponse.builder()
                .isNewMember(false)
                .accessToken(appAccessToken)
                .refreshToken(appRefreshToken)
                .build();
    }

    /**
     * Get alg, kid From JWT Header
     */
    public Map<String, String> getHeaderFromIdToken(String idToken) throws JsonProcessingException {
        String idTokenHeader = idToken.substring(0, idToken.indexOf("."));
        String decodeIdTokenHeader = new String(Base64Utils.decodeFromUrlSafeString(idTokenHeader));
        return objectMapper.readValue(decodeIdTokenHeader, Map.class);
    }

    /**
     * Get Public Key
     */
    public Claims verifyIdToken(String kid, String alg, String idToken) throws BaseException, NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            WebClient webClient = WebClient
                    .builder()
                    .baseUrl(PUBLIC_KEY_URL)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            ApplePublicKeyResponse publicKeyResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/auth/keys")
                            .build())
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RuntimeException("Client Error")))
                    .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RuntimeException("Internal Server Error")))
                    .bodyToMono(ApplePublicKeyResponse.class)
                    .flux()
                    .toStream()
                    .findFirst()
                    .orElseThrow(() -> new BaseException(INVALID_PUBLIC_KEY));
            ApplePublicKeyResponse.Key key = publicKeyResponse.getMatchedKeyBy(kid, alg)
                    .orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));

            byte[] n = Base64Utils.decodeFromUrlSafeString(key.getN());
            byte[] e = Base64Utils.decodeFromUrlSafeString(key.getE());

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, n), new BigInteger(1, e));
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();

        } catch (MalformedJwtException e) {
            throw new BaseException(MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new BaseException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new BaseException(UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new BaseException(INVALID_TOKEN);
        }
    }

    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource(KEY_ID_PATH);
        String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }

    public String makeClientSecret() throws IOException {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setHeaderParam("kid", KEY_ID)
                .setHeaderParam("alg", "ES256")
                .setIssuer(TEAM_ID)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .setAudience("https://appleid.apple.com")
                .setSubject(CLIENT_ID)
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    /**
     * Get Apple Refresh Token
     * For Delete Account
     */
    public String getAppleRefreshToken(String authorizationCode) throws BaseException {
        WebClient webClient = WebClient
                .builder()
                .baseUrl(PUBLIC_KEY_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        AppleTokenResponse appleTokenResponse =
                webClient
                        .post()
                        .uri(uriBuilder -> {
                            try {
                                return uriBuilder
                                        .path("/auth/token")
                                        .queryParam("client_id", CLIENT_ID)
                                        .queryParam("code", authorizationCode)
                                        .queryParam("client_secret", makeClientSecret())
                                        .queryParam("grant_type", "authorization_code")
                                        .build();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .retrieve()
                        .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new RuntimeException("Social Access Token is unauthorized")))
                        .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new RuntimeException("Internal Server Error")))
                        .bodyToMono(AppleTokenResponse.class)
                        .flux()
                        .toStream()
                        .findFirst()
                        .orElseThrow(() -> new BaseException(INVALID_OAUTH_TOKEN));

        return appleTokenResponse.getRefreshToken();
    }

    /**
     * Revoke Apple Login
     */
    public String revoke() throws BaseException {
        String accessToken = JwtHeaderUtil.getAccessToken();
        String handle = jwtService.getHandle(accessToken);

        User user = userRepository.findUserByUserHandle(handle);

        WebClient webClient = WebClient
                .builder()
                .baseUrl(PUBLIC_KEY_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        return webClient
                .post()
                .uri(uriBuilder -> {
                    try {
                        return uriBuilder
                                .path("/auth/revoke")
                                .queryParam("client_id", CLIENT_ID)
                                .queryParam("client_secret", makeClientSecret())
                                .queryParam("token", user.getSocialRefreshToken())
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
