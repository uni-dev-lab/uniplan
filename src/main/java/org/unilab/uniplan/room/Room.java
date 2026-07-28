package org.unilab.uniplan.room;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.building.Building;
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.faculty.Faculty;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "faculty_id", referencedColumnName = "id", nullable = true)
    private Faculty faculty;

    @Column(name = "room_number", nullable = false, length = 50)
    private String roomNumber;

    @Column(name = "available_seats", nullable = false)
    private int availableSeats;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;
}