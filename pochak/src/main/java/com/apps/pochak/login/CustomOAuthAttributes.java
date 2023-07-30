package com.apps.pochak.login;

import com.apps.pochak.user.domain.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class CustomOAuthAttributes {

    private String userNameAttributeName;
    private Map<String, Object> attributes;
    private String name;
    private String email;
    private SocialType socialType;

    @Builder
    public CustomOAuthAttributes(String userNameAttributeName, Map<String, Object> attributes, String name, String email, SocialType socialType) {
        this.userNameAttributeName = userNameAttributeName;
        this.attributes = attributes;
        this.name = name;
        this.email = email;
        this.socialType = socialType;
    }

    public static CustomOAuthAttributes createCustomOAuthAttributes(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if (SocialType.GOOGLE.getKey().equals(registrationId)) {
            return GoogleCustomOAuthAttributes(userNameAttributeName, attributes, SocialType.GOOGLE);
        }
        else {
            throw new IllegalArgumentException("Invalid Provider Registration Id");
        }
    }

    private static CustomOAuthAttributes GoogleCustomOAuthAttributes(String userNameAttributeName, Map<String, Object> attributes, SocialType socialType) {
        return CustomOAuthAttributes.builder()
                .userNameAttributeName(userNameAttributeName)
                .attributes(attributes)
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .socialType(socialType)
                .build();
    }
}
