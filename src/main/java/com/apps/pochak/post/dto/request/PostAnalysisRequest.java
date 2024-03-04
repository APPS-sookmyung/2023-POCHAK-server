package com.apps.pochak.post.dto.request;

import com.apps.pochak.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostAnalysisRequest {
    private int count;
    private List<PostAnalysisElement> postList;

    public PostAnalysisRequest(List<Post> postList) {
        this.count = postList.size();
        this.postList = postList.stream().map(
                PostAnalysisElement::new
        ).collect(Collectors.toList());
    }
}
