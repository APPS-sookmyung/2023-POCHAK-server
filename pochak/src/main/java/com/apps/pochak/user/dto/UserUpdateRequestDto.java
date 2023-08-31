package com.apps.pochak.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateRequestDto {
    private String profileImgUrl;
    private String name;
//    private String handle;
    private String message;
}
