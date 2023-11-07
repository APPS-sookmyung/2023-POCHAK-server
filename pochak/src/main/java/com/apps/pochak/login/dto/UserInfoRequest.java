package com.apps.pochak.login.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile profileImage;

    @Builder
    public UserInfoRequest(String name, String email, String handle, String message, String socialId, String socialType, String socialRefreshToken, MultipartFile profileImage) {
        this.name = name;
        this.email = email;
        this.handle = handle;
        this.message = message;
        this.socialId = socialId;
        this.socialType = socialType;
        this.profileImage = profileImage;
        this.socialRefreshToken = socialRefreshToken;
    }
}
