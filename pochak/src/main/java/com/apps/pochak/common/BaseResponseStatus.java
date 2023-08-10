package com.apps.pochak.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000: Request error
     */
    // user (2000 ~ 2199)
    INVALID_REQUEST(false, 2000, "이름을 입력해주세요."), // example
    INVALID_TOKEN(false, 2001, "잘못된 토큰입니다."),
    INVALID_TOKEN_SIGNATURE(false, 2002, "잘못된 토큰 서명입니다."),
    UNSUPPORTED_TOKEN(false, 2003, "지원하지 않는 형식의 토큰입니다."),
    MALFORMED_TOKEN(false, 2004, "유효하지 않은 구성의 토큰입니다."),
    // comment (2200 ~ 2399)

    // post (2400 ~ 2599)

    // alarm (2600 ~ 2799)

    /**
     * 3000: Response error
     */

    // user (3000 ~ 3199)
    INVALID_USER_ID(false, 3000, "유저를 찾을 수 없습니다."),
    EXIST_USER_ID(false, 3001, "존재하는 유저입니다."),
    EXPIRED_TOKEN(false, 3002, "만료된 토큰입니다."),
    INVALID_USER_INFO(false, 3003, "유저 정보를 가져올 수 없습니다."),
    INVALID_ACCESS_TOKEN(false, 3004, "토큰을 가져올 수 없습니다."),

    // comment (3200 ~ 3399)

    // post (3400 ~ 3599)

    // alarm (3600 ~ 3799)

    /**
     * 4000: DB, Server Error
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
