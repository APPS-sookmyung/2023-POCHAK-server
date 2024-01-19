package com.apps.pochak.login.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class UserInfoRequest implements Serializable {

    private String name;
    private String email;
    private String handle;
    private String message;
    private String socialId;
    private String socialType;
    private String socialRefreshToken;
    private MultipartFile profileImage;
}
