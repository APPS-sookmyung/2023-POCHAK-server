package com.apps.pochak.login.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OAuthResponse {
    private String id;
    private String accessToken;
    private String refreshToken;
    private Boolean isNewMember;
}
