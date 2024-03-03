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
    SUCCESS_UPLOAD_COMMENT(NO_CONTENT, "COMMENT2001", "성공적으로 댓글을 등록하였습니다."),

    // Follow
    SUCCESS_FOLLOW(NO_CONTENT, "FOLLOW2001", "성공적으로 팔로우하였습니다"),
    SUCCESS_UNFOLLOW(NO_CONTENT, "FOLLOW2002", "성공적으로 팔로우를 취소하였습니다."),
    SUCCESS_DELETE_FOLLOWER(NO_CONTENT, "FOLLOW2003", "성공적으로 팔로워를 삭제하였습니다."),

    // Like
    SUCCESS_LIKE(NO_CONTENT, "LIKE2001", "성공적으로 좋아요를 처리하였습니다."),

    // Login
    SUCCESS_LOG_OUT(NO_CONTENT, "LOGIN2001", "성공적으로 로그아웃하였습니다"),
    SUCCESS_SIGN_OUT(NO_CONTENT, "LOGIN2002", "성공적으로 탈퇴하였습니다."),

    // Member

    // Post
    SUCCESS_UPLOAD_POST(NO_CONTENT, "POST2001", "성공적으로 게시물을 등록하였습니다."),
    SUCCESS_DELETE_POST(NO_CONTENT, "POST2002", "성공적으로 게시물을 삭제하였습니다.");

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
