package org.unilab.uniplan.roomcategory;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.category.Category;
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.room.Room;

@Entity
@Table(name = "ROOM_CATEGORY",  
    uniqueConstraints=
    @UniqueConstraint(columnNames={"ROOM_ID", "CATEGORY_ID"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomCategory extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "ID", nullable = false)
    private Room room;
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID", nullable = false)
    private Category category;
}