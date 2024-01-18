package com.apps.pochak.global;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static com.apps.pochak.global.BaseEntityStatus.ACTIVE;
import static com.apps.pochak.global.BaseEntityStatus.DELETED;
import static jakarta.persistence.EnumType.STRING;

@Getter
@MappedSuperclass
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Enumerated(STRING)
    @Setter
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'ACTIVE'")
    private BaseEntityStatus status;

    public BaseEntityStatus toggleCurrentStatus() {
        if (this.status.equals(ACTIVE)) {
            this.status = DELETED;
            return DELETED;
        } else {
            this.status = ACTIVE;
            return ACTIVE;
        }
    }
}
