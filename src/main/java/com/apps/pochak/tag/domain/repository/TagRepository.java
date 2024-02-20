package com.apps.pochak.tag.domain.repository;

import com.apps.pochak.post.domain.Post;
import com.apps.pochak.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("select t from Tag t " +
            "join fetch t.member " +
            "where t.post = :post ")
    List<Tag> findTagByPost(@Param("post") Post post);

    @Modifying
    @Query("update Tag tag " +
            "set tag.status = 'DELETED' " +
            "where tag.member.id = :memberId or tag.post.owner.id = :memberId")
    void deleteTagByMemberId(@Param("memberId") final Long memberId);

}
