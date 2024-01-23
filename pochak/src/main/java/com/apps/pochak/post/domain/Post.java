package com.apps.pochak.post.domain;

import com.apps.pochak.global.BaseEntity;
import com.apps.pochak.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE post SET status = 'DELETED' WHERE id = ?")
@SQLRestriction("status = 'ACTIVE'")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'PRIVATE'")
    private PostStatus postStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    private String postImage;
    private String caption;

    @Builder
    public Post(Member owner, String postImage, String caption) {
        this.owner = owner;
        this.postImage = postImage;
        this.caption = caption;
    }
}
