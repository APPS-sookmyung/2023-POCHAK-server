package com.apps.pochak.alarm.domain;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.member.domain.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("COMMENT")
public class CommentAlarm extends Alarm {
    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public CommentAlarm(Member receiver, Comment comment) {
        super(receiver);
        this.comment = comment;
    }
}