package com.apps.pochak.login.jwt;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.login.dto.PostTokenResponse;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.apps.pochak.common.BaseResponseStatus.*;
import static com.apps.pochak.common.Constant.AUTHORITIES_KEY;

@Getter
@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserRepository userRepository;
    private final long accessTokenExpirationTime = 1800000;
    private final long refreshTokenExpirationTime = 1000L * 60 * 60 * 24 * 30;
    @Value("${jwt.secretKey}")
    private String secretKey;
    private Key key;

    @PostConstruct
    private void _getSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKey.getBytes());
        key = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    public String createAccessToken(String userPK) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userPK)
                .claim(AUTHORITIES_KEY, "ROLE_USER")
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
        User user = userRepository.findUserByUserHandle(handle);
        if (!user.getRefreshToken().equals(refreshToken)) {
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

    public Claims getTokenClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getHandle(String token) {
        return getTokenClaims(token).getSubject();
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
