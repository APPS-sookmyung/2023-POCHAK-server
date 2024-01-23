package com.apps.pochak.alarm.domain;

import com.apps.pochak.global.BaseEntity;
import com.apps.pochak.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Getter
@DynamicInsert
@BatchSize(size = 100)
@Inheritance(strategy = SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE alarm SET status = 'DELETED' WHERE id = ?")
@SQLRestriction("status = 'ACTIVE'")
@DiscriminatorColumn(name = "alarmType")
public abstract class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    private Boolean isChecked;

    public Alarm(final Member receiver) {
        this.receiver = receiver;
        this.isChecked = false;
    }
}
