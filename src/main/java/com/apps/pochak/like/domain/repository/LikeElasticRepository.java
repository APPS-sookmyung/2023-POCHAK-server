package com.apps.pochak.like.domain.repository;

import com.apps.pochak.global.elasticsearch.repository.ElasticsearchRepository;
import com.apps.pochak.like.domain.LikeDocument;

public interface LikeElasticRepository extends ElasticsearchRepository<LikeDocument, String> {
}
