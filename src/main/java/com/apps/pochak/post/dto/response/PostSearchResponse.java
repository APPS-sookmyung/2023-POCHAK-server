package com.apps.pochak.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchResponse {
    private int count;
    private List<Long> postIdList;
}
