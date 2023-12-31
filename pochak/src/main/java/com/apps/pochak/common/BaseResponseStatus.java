package com.apps.pochak.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000: Success
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    // user (1001 ~ 1199)
    NULL_FOLLOW_STATUS(true, 1001, "조회한 프로필이 로그인한 유저의 프로필이기 때문에, 팔로우 여부는 제공되지 않습니다."),


    // comment (1200 ~ 1399)
    NULL_COMMENTS(true, 1200, "아직 게시물에 댓글이 등록되지 않았기에, 미리보기 댓글이 제공되지 않습니다."),

    // post (1400 ~ 1599)
    NULL_TAGGED_POST(true, 1400, "아직 태그된 포스트가 없습니다."),
    NULL_UPLOAD_POST(true, 1401, "아직 업로드한 포스트가 없습니다."),
    SUCCESS_LIKE(true, 1402, "성공적으로 좋아요를 추가하였습니다."),
    CANCEL_LIKE(true, 1403, "성공적으로 좋아요를 취소하였습니다."),
    LAST_PUBLISH_PAGE(true, 1404, "업로드한 포스트 리스트의 마지막 페이지입니다."),
    LAST_TAG_PAGE(true, 1405, "태그된 포스트 리스트의 마지막 페이지입니다."),

    // alarm (1600 ~ 1799)
    ALL_ALLOW_POST(true, 1600, "모든 사람들이 포스트 공개를 수락했습니다."),

    /**
     * 2000: Request error
     */
    // user (2000 ~ 2199)
    INVALID_LOGIN_INFO(false, 2000, "유저의 로그인 정보가 유효하지 않습니다."),
    NULL_USER_HANDLE(false, 2001, "유저의 handle을 입력해주세요"),
    INVALID_UPDATE_REQUEST(false, 2002, "프로필은 당사자만 업데이트할 수 있습니다. API 요청을 다시 확인해주세요."),
    NULL_USER_NAME(false, 2003, "유저 이름을 입력해주세요"),
    FOLLOW_ONESELF(false, 2004, "다른 사람만 팔로우할 수 있습니다."),
    INVALID_FOLLOWER(false, 2005, "현재 로그인 된 유저를 팔로우하고 있지 않은 유저입니다. API 요청을 다시 확인해주세요"),
    INVALID_TOKEN(false, 2006, "잘못된 토큰입니다."),
    INVALID_TOKEN_SIGNATURE(false, 2007, "잘못된 토큰 서명입니다."),
    UNSUPPORTED_TOKEN(false, 2008, "지원하지 않는 형식의 토큰입니다."),
    MALFORMED_TOKEN(false, 2009, "유효하지 않은 구성의 토큰입니다."),
    NULL_TOKEN(false, 2010, "토큰이 존재하지 않습니다."),
    EXIST_USER(false, 2011, "존재하는 유저입니다."),

    // comment (2200 ~ 2399)
    INVALID_COMMENT_SK(false, 2200, "잘못된 Comment Sort Key 입니다."),
    NOT_YOUR_COMMENT(false, 2201, "자신의 댓글이 아니므로 지울 수 없습니다."),



    // post (2400 ~ 2599)
    NULL_TAGGED_USER(false, 2400, "유저를 태그해주세요"),
    NULL_IMAGE(false, 2401, "사진 url을 입력해주세요"),
    NOT_YOUR_POST(false, 2402, "자신의 게시글이 아니므로 지울 수 없습니다."),
    NOT_ALLOW_POST(false, 2403, "해당 게시글을 접근할 수 있는 권한이 없습니다."),

    // alarm (2600 ~ 2799)
    INVALID_ALARM_ID(false, 2600, "알람을 찾을 수 없습니다."),
    PUBLISH_ALLOWED_POST(false, 2601, "이미 공개 수락된 포스트입니다."),

    /**
     * 3000: Response error
     */
    // user (3000 ~ 3199)
    INVALID_USER_HANDLE(false, 3000, "주어진 handle로 유저를 찾을 수 없습니다."),
    INVALID_PUBLIC_KEY(false, 3001, "공개키를 가져올 수 없습니다."),
    EXPIRED_TOKEN(false, 3002, "만료된 토큰입니다."),
    INVALID_USER_INFO(false, 3003, "유저 정보를 가져올 수 없습니다."),
    INVALID_OAUTH_TOKEN(false, 3004, "토큰을 가져올 수 없습니다."),

    // comment (3200 ~ 3399)
    INVALID_COMMENT_ID(false, 3200, "댓글을 찾을 수 없습니다."),

    // post (3400 ~ 3599)
    INVALID_POST_ID(false, 3400, "포스트를 찾을 수 없습니다."),
    DELETED_POST(false, 3401, "삭제된 게시글이므로 조회가 불가능합니다."),

    // alarm (3600 ~ 3799)
    POST_OWNER_LIKE(false, 2600, "포스트의 owner는 좋아요 누르기가 불가능합니다."),

    /**
     * 4000: DB, Server Error
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다"),
    DUMMY_USER_ID(false, 4001, "데이터베이스에 더미 UserID 데이터가 들어가있습니다"),
    RESOURCE_NOT_FOUND(false, 4002, "The operation tried to access a nonexistent table or index. The resource might not be specified correctly, or its status might not be ACTIVE."),
    INVALID_ALARM_TYPE(false, 4003, "데이터베이스에 유효하지 않은 타입에 해당하는 알람 데이터가 들어있습니다"),
    Delete_File_Error(false, 4004, "파일 삭제를 실패하였습니다."),
    S3_Upload_Error(false, 4005, "S3 업로드를 실패했습니다."),
    Convert_File_Error(false, 4006, "MultipartFile을 File로 전환 실패했습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
