package com.apps.pochak.login.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostTokenResponse {
    private String accessToken;
}
