package com.apps.pochak.global.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DirName {
    MEMBER("member"), POST("post");

    private final String dirName;
}
