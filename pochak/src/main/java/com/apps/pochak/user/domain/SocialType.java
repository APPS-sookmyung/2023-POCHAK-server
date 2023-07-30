package com.apps.pochak.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {

    GOOGLE("google"), APPLE("apple");

    private final String key;
}