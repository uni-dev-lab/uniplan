package org.unilab.uniplan.major;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "majors")
public class Major extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "FACULTY_ID", nullable = false)
    private Faculty faculty;

    @Column(name = "MAJOR_NAME", nullable = false, length = 200)
    private String majorName;
}
