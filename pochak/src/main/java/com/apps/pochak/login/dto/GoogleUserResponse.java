package com.apps.pochak.login.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUserResponse {
    public String id;
    public String name;
    public String email;
}
