package com.apps.pochak.post.service;

import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.domain.repository.PostRepository;
import com.apps.pochak.post.dto.request.PostAnalysisRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostScheduler {

    @Value("${lambda.baseUrl}")
    private String lambdaBaseUrl;

    private final RestTemplate restTemplate;

    private final PostRepository postRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void savePostData() {
        final List<Post> postList
                = postRepository.findModifiedPostWithinOneHour(LocalDateTime.now().minusHours(1));

        final PostAnalysisRequest body = new PostAnalysisRequest(postList);

        restTemplate.postForObject(lambdaBaseUrl + "", body, null); // TODO: URL 수정
    }

}
