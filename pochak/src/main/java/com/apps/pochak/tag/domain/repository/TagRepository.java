package com.apps.pochak.tag.domain.repository;

import com.apps.pochak.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
