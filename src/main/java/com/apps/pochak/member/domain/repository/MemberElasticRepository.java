package com.apps.pochak.member.domain.repository;

import com.apps.pochak.global.elasticsearch.repository.ElasticsearchRepository;
import com.apps.pochak.member.domain.MemberDocument;

public interface MemberElasticRepository extends ElasticsearchRepository<MemberDocument, String> {
}
