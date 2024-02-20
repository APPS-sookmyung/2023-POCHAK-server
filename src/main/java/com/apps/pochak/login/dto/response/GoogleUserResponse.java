package com.apps.pochak.login.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUserResponse {
    // TODO: User -> Member 이름 변경
    public String id;
    public String name;
    public String email;
}
