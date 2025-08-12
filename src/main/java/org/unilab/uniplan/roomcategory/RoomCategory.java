package org.unilab.uniplan.roomcategory;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.category.Category;
import org.unilab.uniplan.common.model.AuditableEntity;
import org.unilab.uniplan.room.Room;

@Entity
@Table(name = "room_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomCategory extends AuditableEntity {

    @EmbeddedId
    private RoomCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = false)
    private Category category;
}