package com.apps.pochak.post.service;

import com.apps.pochak.post.dto.PostUploadRequestDto;
import com.apps.pochak.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;

}
