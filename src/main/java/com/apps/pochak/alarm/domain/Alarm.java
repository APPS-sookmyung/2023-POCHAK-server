package com.apps.pochak.alarm.domain;

import com.apps.pochak.comment.domain.Comment;
import com.apps.pochak.follow.domain.Follow;
import com.apps.pochak.global.BaseEntity;
import com.apps.pochak.like.domain.LikeEntity;
import com.apps.pochak.member.domain.Member;
import com.apps.pochak.tag.domain.Tag;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@DynamicInsert
@SQLDelete(sql = "UPDATE alarm SET status = 'DELETED' WHERE id = ?")
@SQLRestriction("status = 'ACTIVE'")
@NoArgsConstructor(access = PROTECTED)
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Column(columnDefinition = "boolean default false")
    private Boolean isChecked;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "follow_id")
    private Follow follow;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "like_id")
    private LikeEntity like;

    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "tag_approval_id")
    private Tag tag;

    @Builder(builderMethodName = "commentAlarmBuilder")
    public Alarm(Comment comment, Member receiver) {
        this.comment = comment;
        this.receiver = receiver;
        this.alarmType = AlarmType.COMMENT;
    }

    @Builder(builderMethodName = "followAlarmBuilder")
    public Alarm(Follow follow, Member receiver) {
        this.follow = follow;
        this.receiver = receiver;
        this.alarmType = AlarmType.FOLLOW;
    }

    @Builder(builderMethodName = "likeAlarmBuilder")
    public Alarm(LikeEntity like, Member receiver) {
        this.like = like;
        this.receiver = receiver;
        this.alarmType = AlarmType.LIKE;
    }

    @Builder(builderMethodName = "tagApprovalAlarmBuilder")
    public Alarm(Tag tag, Member receiver) {
        this.tag = tag;
        this.receiver = receiver;
        this.alarmType = AlarmType.TAG_APPROVAL;
    }
}
