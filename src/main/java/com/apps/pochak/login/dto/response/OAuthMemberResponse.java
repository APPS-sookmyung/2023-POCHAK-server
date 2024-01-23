package com.apps.pochak.login.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class OAuthMemberResponse {
    private final String id;
    private final String name;
    private final String email;
    private final String socialType;
    private final String accessToken;
    private final String refreshToken;
    private final Boolean isNewMember;

    @Builder
    public static OAuthMemberResponse of(
            final String socialId,
            final String name,
            final String email,
            final String socialType,
            final String accessToken,
            final String refreshToken,
            final Boolean isNewMember
    ) {
        return new OAuthMemberResponse(
                socialId,
                name,
                email,
                socialType,
                accessToken,
                refreshToken,
                isNewMember
        );
    }
}
