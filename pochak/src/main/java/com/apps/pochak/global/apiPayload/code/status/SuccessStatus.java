package com.apps.pochak.global.apiPayload.code.status;

import com.apps.pochak.global.apiPayload.code.BaseCode;
import com.apps.pochak.global.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    // common
    _OK(OK, "COMMON200","성공입니다."),

    // login
    SUCCESS_LOG_OUT(NO_CONTENT, "LOGIN2001", "성공적으로 로그아웃하였습니다"),
    SUCCESS_SIGN_OUT(NO_CONTENT, "LOGIN2002", "성공적으로 탈퇴하였습니다.");

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
