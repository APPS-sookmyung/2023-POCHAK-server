package com.apps.pochak.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserUpdateResDto {
    private String profileImgUrl;
    private String name;
    private String handle;
    private String message;

    @Builder
    public UserUpdateResDto(String profileImgUrl, String name, String handle, String message) {
        this.profileImgUrl = profileImgUrl;
        this.name = name;
        this.handle = handle;
        this.message = message;
    }
}
