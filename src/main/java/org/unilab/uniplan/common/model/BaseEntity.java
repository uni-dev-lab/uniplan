package org.unilab.uniplan.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity extends AuditableEntity{

    @Id
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    private UUID getId() {
        return id;
    }

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Class<?> thisEffectiveClass = (this instanceof HibernateProxy proxy)
            ? proxy.getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();

        Class<?> otherEffectiveClass = (o instanceof HibernateProxy proxy)
            ? proxy.getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
        if (!thisEffectiveClass.equals(otherEffectiveClass)) return false;
        BaseEntity other = (BaseEntity) o;

        return getId() != null && getId().equals(other.getId());
    }
    @Override
    public final int hashCode() {
        return (this instanceof HibernateProxy proxy)
            ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
    }
}

