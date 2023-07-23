package com.apps.pochak.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {
    private String profileImgUrl;
    private String name;
    private String nickname;
    private String message;

}
