package com.apps.pochak.login.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthResponse {
    private String id;
    private String name;
    private String email;
    private String socialType;
    private String accessToken;
    private String refreshToken;
    private Boolean isNewMember;
}
