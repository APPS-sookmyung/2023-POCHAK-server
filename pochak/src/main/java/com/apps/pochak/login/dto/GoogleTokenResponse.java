package com.apps.pochak.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleTokenResponse {

    private String scope;

    @JsonProperty(value = "expires_in")
    private int expiresIn;

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "number_of_employees")
    private String numberOfEmployees;
}
