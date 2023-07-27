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

    // comment (2200 ~ 2399)

    // post (2400 ~ 2599)

    // alarm (2600 ~ 2799)

    /**
     * 3000: Response error
     */

    // user (2000 ~ 2199)
    INVALID_USER_ID(false, 2000, "유저를 찾을 수 없습니다."), // example

    // comment (2200 ~ 2399)

    // post (2400 ~ 2599)

    // alarm (2600 ~ 2799)

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