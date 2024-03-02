package com.apps.pochak.like.service;

import com.apps.pochak.like.domain.LikeDocument;
import com.apps.pochak.like.domain.LikeEntity;
import com.apps.pochak.like.domain.repository.LikeElasticRepository;
import com.apps.pochak.like.domain.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeElasticScheduler {
    private final LikeRepository likeRepository;
    private final LikeElasticRepository likeElasticRepository;

    @Scheduled(cron = "0 22 * * * *")
    public void saveLikeData() {
        System.out.println(LocalDateTime.now() + " : 실행"); // Check

        final List<LikeEntity> likeList
                = likeRepository.findModifiedLikeEntityWithinOneHour(LocalDateTime.now().minusHours(1));

        final List<LikeDocument> likeDocumentList = likeList.stream().map(
                LikeDocument::new
        ).collect(Collectors.toList());

        likeElasticRepository.saveAll(likeDocumentList);
    }
}
