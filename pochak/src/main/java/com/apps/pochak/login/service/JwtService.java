package com.apps.pochak.login.service;

import com.apps.pochak.common.BaseException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

import static com.apps.pochak.common.BaseResponseStatus.*;

@Getter
@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;
    private String token;
    private final long accessTokenExpirationTime = 1800000;
    private final long refreshTokenExpirationTime = 1800000;

    public Key getKey() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String createAccessToken(String userPK) {
        Claims claims = Jwts.claims().setSubject(userPK);
        Key key = getKey();
        Date now = new Date();

        return token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String userPK) {
        Date now = new Date();
        Key key = getKey();

        return token = Jwts.builder()
                .setExpiration(new Date(now.getTime() + refreshTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateRefreshToken(String userPK, String refreshToken) throws BaseException {
        return this.getClaims() != null;
    }

    public Claims getClaims() throws BaseException {
        Key key = getKey();
        Claims claims = null;

        try {
            claims = Jwts.parserBuilder()
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
        return claims;
    }
}
