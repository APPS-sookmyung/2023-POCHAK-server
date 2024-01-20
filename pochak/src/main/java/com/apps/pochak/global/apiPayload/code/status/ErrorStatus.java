package com.apps.pochak.global.apiPayload.code.status;

import com.apps.pochak.global.apiPayload.code.BaseErrorCode;
import com.apps.pochak.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED, "COMMON401", "인증이 필요합니다. 권한을 확인해주세요."),
    _FORBIDDEN(FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // Global
    IO_EXCEPTION(INTERNAL_SERVER_ERROR, "COMMON5001", "서버 IO Exception 발생, 관리자에게 문의 바랍니다"),

    // Alarm

    // Comment

    // Follow
    NOT_FOLLOW(INTERNAL_SERVER_ERROR, "FOLLOW4001", "데이터에러: 팔로우 상태를 찾을 수 없습니다 - 상대방이 팔로우하고 있지 않습니다."),

    // Like

    // Login
    INVALID_TOKEN(BAD_REQUEST, "LOGIN4001", "잘못된 엑세스 토큰입니다."),
    INVALID_TOKEN_SIGNATURE(BAD_REQUEST, "LOGIN4002", "잘못된 토큰 서명입니다."),
    UNSUPPORTED_TOKEN(BAD_REQUEST, "LOGIN4003", "지원하지 않는 형식의 토큰입니다."),
    MALFORMED_TOKEN(BAD_REQUEST, "LOGIN4004", "유효하지 않은 구성의 토큰입니다."),
    NULL_TOKEN(BAD_REQUEST, "LOGIN4005", "토큰이 존재하지 않습니다."),
    EXIST_USER(BAD_REQUEST, "LOGIN4006", "존재하는 유저입니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "LOGIN4007", "잘못된 리프레시 토큰입니다."),
    NULL_REFRESH_TOKEN(BAD_REQUEST, "LOGIN4008", "리프레시 토큰이 존재하지 않습니다."),
    EXPIRED_TOKEN(BAD_REQUEST, "LOGIN4009", "만료된 토큰입니다."),
    INVALID_USER_HANDLE(BAD_REQUEST, "LOGIN4010", "주어진 handle로 유저를 찾을 수 없습니다."),
    INVALID_PUBLIC_KEY(BAD_REQUEST, "LOGIN4011", "공개키를 가져올 수 없습니다."),
    INVALID_USER_INFO(BAD_REQUEST, "LOGIN4012", "유저 정보를 가져올 수 없습니다."),
    INVALID_OAUTH_TOKEN(BAD_REQUEST, "LOGIN4013", "토큰을 가져올 수 없습니다."),

    // Member
    INVALID_MEMBER_HANDLE(BAD_REQUEST, "MEMBER4001", "유효하지 않은 멤버의 handle입니다."),

    // Post
    INVALID_POST_ID(BAD_REQUEST, "POST4001", "유효하지 않은 POST ID 입니다."),
    PRIVATE_POST(BAD_REQUEST, "POST4002", "공개되지 않은 게시물입니다. 접근 권한이 없습니다."),

    // Tag

    // Image
    DELETE_FILE_ERROR(SERVICE_UNAVAILABLE, "IMAGE501", "파일 삭제를 실패하였습니다."),
    S3_UPLOAD_ERROR(SERVICE_UNAVAILABLE, "IMAGE502", "S3 업로드를 실패하였습니다."),
    CONVERT_FILE_ERROR(SERVICE_UNAVAILABLE, "IMAGE503", "MultipartFile을 File로 전환 실패하였습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
