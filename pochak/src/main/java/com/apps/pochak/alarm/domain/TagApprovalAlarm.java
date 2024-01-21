package com.apps.pochak.alarm.domain;

import com.apps.pochak.member.domain.Member;
import com.apps.pochak.tag.domain.Tag;
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
@DiscriminatorValue("TAG_APPROVAL")
public class TagApprovalAlarm extends Alarm {
    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "tag_approval_id")
    private Tag tag;

    @Builder
    public TagApprovalAlarm(Member receiver, Tag tag) {
        super(receiver);
        this.tag = tag;
    }
}
