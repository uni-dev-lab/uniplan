package org.unilab.uniplan.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.BaseEntity;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    @Column(name = "room_type", nullable = false, length = 50)
    private String roomType;
    @Column(name = "capacity", nullable = false)
    private short capacity;
}