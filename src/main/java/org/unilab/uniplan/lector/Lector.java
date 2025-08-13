package org.unilab.uniplan.lector;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.Person;
import org.unilab.uniplan.faculty.Faculty;

@Entity
@Table(name = "lector")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lector extends Person {

    @Column(name = "email", nullable = false, length = 250)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", referencedColumnName = "id", nullable = false)
    private Faculty faculty;
}

