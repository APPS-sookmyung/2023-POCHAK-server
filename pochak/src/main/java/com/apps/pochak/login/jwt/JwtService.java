package com.apps.pochak.login.jwt;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.login.dto.PostTokenResponse;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.apps.pochak.common.BaseResponseStatus.*;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;
    private final long accessTokenExpirationTime = 2000;
    private final long refreshTokenExpirationTime = 2000;
    @Value("${jwt.secretKey}")
    private String secretKey;
    private Key key;

    @PostConstruct
    private void _getSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKey.getBytes());
        key = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    public String createAccessToken(String userPK) throws BaseException {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userPK)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + refreshTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateRefreshToken(String accessToken, String refreshToken) throws BaseException {
        String handle = getHandle(accessToken);
        User user = userRepository.findUserWithUserHandle(handle);
        if (user.getRefreshToken() != refreshToken) {
            throw new BaseException(INVALID_TOKEN);
        }
        return user.getHandle();
    }

    public boolean validate(String token) throws BaseException {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (SecurityException e) {
            throw new BaseException(INVALID_TOKEN_SIGNATURE);
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

    public String getHandle(String token) throws BaseException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public PostTokenResponse reissueAccessToken() throws BaseException {
        String accessToken = JwtHeaderUtil.getAccessToken();
        String refreshToken = JwtHeaderUtil.getRefreshToken();

        if (!validate(refreshToken)) {
            throw new BaseException(INVALID_TOKEN);
        }

        String handle = validateRefreshToken(accessToken, refreshToken);
        String newAccessToken = createAccessToken(handle);

        return PostTokenResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }
}
