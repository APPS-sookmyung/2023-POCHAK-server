package com.apps.pochak.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class UserUpdateRequestDto {
    private MultipartFile profileImgUrl;
    private String name;
//    private String handle;
    private String message;
}
