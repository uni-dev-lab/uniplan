package org.unilab.uniplan.lector;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.Person;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LECTOR")
public class Lector extends Person {

    @Column(name = "EMAIL", nullable = false)
    private String email;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "FACULTY_ID", nullable = false)
    //private Faculty faculty;
}

