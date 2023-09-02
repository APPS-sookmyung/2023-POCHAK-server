package com.apps.pochak.publish.service;

import com.apps.pochak.publish.repository.PublishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishService {
    private final PublishRepository publishRepository;
}
