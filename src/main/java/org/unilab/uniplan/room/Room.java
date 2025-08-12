package org.unilab.uniplan.room;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.faculty.Faculty;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "FACULTY_ID", referencedColumnName = "ID", nullable = false)
    private Faculty faculty;
    @Column(name = "ROOM_NUMBER", nullable = false, length = 50)
    private String roomNumber;

}