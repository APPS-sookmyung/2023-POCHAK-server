package com.apps.pochak.login.service;

import com.apps.pochak.login.CustomOAuth2User;
import com.apps.pochak.login.CustomOAuthAttributes;
import com.apps.pochak.user.domain.Role;
import com.apps.pochak.user.domain.User;
import com.apps.pochak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@EnableWebSecurity
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        CustomOAuthAttributes customOAuthAttributes = CustomOAuthAttributes.createCustomOAuthAttributes(registrationId, userNameAttributeName, attributes);

        User user = checkUser(customOAuthAttributes);

        return new CustomOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())), attributes, userNameAttributeName, user.getRole(), user.getEmail());
    }

    private User checkUser(CustomOAuthAttributes customOAuthAttributes) {
        User user = userRepository.findUserByEmail(customOAuthAttributes.getEmail());

        if (user == null) {
            return createUser(customOAuthAttributes);
        }
        return user;
    }

    private User createUser(CustomOAuthAttributes customOAuthAttributes) {
        User user = User.builder()
                .name(customOAuthAttributes.getName())
                .email(customOAuthAttributes.getEmail())
                .socialType(customOAuthAttributes.getSocialType())
                .role(Role.GUEST)
                .build();
        return userRepository.saveUser(user);
    }
}
