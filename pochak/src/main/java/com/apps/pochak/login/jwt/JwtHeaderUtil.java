package com.apps.pochak.login.jwt;

import com.apps.pochak.common.BaseException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.apps.pochak.common.BaseResponseStatus.NULL_TOKEN;
import static com.apps.pochak.common.Constant.*;

public class JwtHeaderUtil {

    public static String getAccessToken() throws BaseException {
        return getToken(HEADER_AUTHORIZATION);
    }

    public static String getRefreshToken() throws BaseException {
        return getToken(HEADER_REFRESH_TOKEN);
    }

    private static String getToken(String tokenHeader) throws BaseException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String headerValue = request.getHeader(tokenHeader);
        if (headerValue == null) throw new BaseException(NULL_TOKEN);
        if (StringUtils.hasText(headerValue) && headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
