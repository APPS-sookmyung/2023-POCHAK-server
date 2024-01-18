package com.apps.pochak.global.apiPayload.code.status;

import com.apps.pochak.global.apiPayload.code.BaseCode;
import com.apps.pochak.global.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    // common
    _OK(OK, "COMMON200", "성공입니다."),

    // Alarm

    // Comment

    // Follow
    SUCCESS_FOLLOW(NO_CONTENT, "FOLLOW2001", "성공적으로 팔로우하였습니다"),
    SUCCESS_UNFOLLOW(NO_CONTENT, "FOLLOW2002", "성공적으로 팔로우를 취소하였습니다."),
    SUCCESS_DELETE_FOLLOWER(NO_CONTENT, "FOLLOW2003", "성공적으로 팔로워를 삭제하였습니다."),

    // Like

    // Login
    SUCCESS_LOG_OUT(NO_CONTENT, "LOGIN2001", "성공적으로 로그아웃하였습니다"),
    SUCCESS_SIGN_OUT(NO_CONTENT, "LOGIN2002", "성공적으로 탈퇴하였습니다.");

    // Member

    // Post

    // Tag

    // Image

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
