package com.apps.pochak.login.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoRequest {
    private String handle;
    private String message;
    private String socialId;
    private String profileImage;

    @Builder
    public UserInfoRequest(String handle, String message, String socialId, String profileImage) {
        this.handle = handle;
        this.message = message;
        this.socialId = socialId;
        this.profileImage = profileImage;
    }
}
