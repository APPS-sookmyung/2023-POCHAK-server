package com.apps.pochak.login.service;

import com.apps.pochak.common.BaseException;
import com.apps.pochak.login.dto.OAuthResponse;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

import static com.apps.pochak.common.BaseResponseStatus.*;
import static com.apps.pochak.common.Constant.*;

@Getter
@Service
@RequiredArgsConstructor
public class JwtService {

    private final Key key;
    private final UserRepository userRepository;
    private final long accessTokenExpirationTime = 1800000;
    private final long refreshTokenExpirationTime = 1800000;

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

    public String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);
        if (StringUtils.hasText(headerValue) && headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public String getRefreshToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_REFRESH_TOKEN);
        if (StringUtils.hasText(headerValue) && headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validate(String token) throws BaseException {
        return this.getClaims(token) != null;
    }

    public Claims getClaims(String token) throws BaseException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
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

    public OAuthResponse updateRefreshToken(String refreshToken) throws BaseException {
        User user = userRepository.findUserWithRefreshToken(refreshToken)
                .orElseThrow(() -> new BaseException(INVALID_USER_ID));

        if (user != null) {
            String newRefreshToken = createRefreshToken();
            String newAccessToken = createAccessToken(user.getUserPK());
            user.setRefreshToken(newRefreshToken);
            userRepository.updateUser(user);
            return OAuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }
        return null;
    }

    public OAuthResponse reissueToken(HttpServletRequest request) throws BaseException {
        String accessToken = getAccessToken(request);
        if (!validate(accessToken)) {
            return null;
        }

        String refreshToken = getRefreshToken(request);
        if (!validate(refreshToken)) {
            return null;
        }

        String userPK = getClaims(refreshToken).getSubject();
        return updateRefreshToken(userPK);
    }
}
