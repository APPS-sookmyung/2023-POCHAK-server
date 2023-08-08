package com.apps.pochak.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000: Request error
     */
    // user (2000 ~ 2199)
    NULL_USER_HANDLE(false, 2000, "유저의 handle을 입력해주세요"),
    INVALID_UPDATE_REQUEST(false, 2001, "프로필은 당사자만 업데이트할 수 있습니다. API 요청을 다시 확인해주세요."),
    NULL_USER_NAME(false, 2002, "유저 이름을 입력해주세요"),

    // comment (2200 ~ 2399)

    // post (2400 ~ 2599)

    // alarm (2600 ~ 2799)

    /**
     * 3000: Response error
     */

    // user (3000 ~ 3199)
    INVALID_USER_HANDLE(false, 3000, "주어진 handle로 유저를 찾을 수 없습니다."),

    // comment (3200 ~ 3399)
    INVALID_COMMENT_ID(false, 3200, "댓글을 찾을 수 없습니다."),

    // post (3400 ~ 3599)
    INVALID_POST_ID(false,3400,"포스트를 찾을 수 없습니다."),

    // alarm (3600 ~ 3799)

    /**
     * 4000: DB, Server Error
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다"),
    INVALID_USER_ID(false, 4001, "데이터베이스에 더미 UserID 데이터가 들어가있습니다");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
