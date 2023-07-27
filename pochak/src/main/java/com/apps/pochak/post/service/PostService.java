package com.apps.pochak.post.service;

import com.apps.pochak.post.domain.Post;
import com.apps.pochak.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post savePost(Post post) { return postRepository.savePost(post); }
}
