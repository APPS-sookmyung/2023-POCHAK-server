package com.apps.pochak.login.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoRequest {

    private String name;
    private String email;
    private String handle;
    private String message;
    private String socialId;
    private String socialType;
    private String socialRefreshToken;

    @Builder
    public UserInfoRequest(String name, String email, String handle, String message, String socialId, String socialType, String socialRefreshToken) {
        this.name = name;
        this.email = email;
        this.handle = handle;
        this.message = message;
        this.socialId = socialId;
        this.socialType = socialType;
        this.socialRefreshToken = socialRefreshToken;
    }
}
