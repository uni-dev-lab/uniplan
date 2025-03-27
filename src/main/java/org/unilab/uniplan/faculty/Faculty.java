package org.unilab.uniplan.faculty;

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
import org.unilab.uniplan.university.University;

@Entity
@Table(name = "FACULTY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Faculty extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "UNI_ID", referencedColumnName = "ID", nullable = false)
    private University university;
    @Column(name = "FACULTY_NAME", nullable = false, length = 200)
    private String facultyName;
    @Column(name = "LOCATION", length = 500)
    private String location;
}