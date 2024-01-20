package com.apps.pochak.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberElement {
    private String profileImage;
    private String handle;
    private String name;
    private Boolean isFollow;
}
