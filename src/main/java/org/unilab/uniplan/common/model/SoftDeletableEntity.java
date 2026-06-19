package org.unilab.uniplan.common.model;

import jakarta.persistence.Column;
import org.hibernate.annotations.SQLRestriction;
import java.time.LocalDateTime;


@SQLRestriction("deleted_at IS NULL")
public abstract class SoftDeletableEntity extends BaseEntity {
    //I'm using this class for only the Major entity, but if soft delete logic needs to be implemented in other entities, you could use this

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
