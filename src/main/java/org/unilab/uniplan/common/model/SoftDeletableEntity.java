package org.unilab.uniplan.common.model;

import jakarta.persistence.Column;
import java.time.LocalDateTime;

public abstract class SoftDeletableEntity extends BaseEntity {

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
