package com.apps.pochak.login;

import com.apps.pochak.user.domain.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private Role role;
    private String email;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, Role role, String email) {
        super(authorities, attributes, nameAttributeKey);
        this.role = role;
        this.email = email;
    }
}
