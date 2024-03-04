package com.apps.pochak.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LikeElements {

    private final List<LikeElement> likeMembersList;
}
