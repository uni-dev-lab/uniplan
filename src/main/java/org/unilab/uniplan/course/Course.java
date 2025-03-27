package org.unilab.uniplan.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.major.Major;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COURSE")
public class Course extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "MAJOR_ID", nullable = false)
    private Major major;

    @Column(name = "COURSE_YEAR", nullable = false)
    @Min(1)
    @Max(20)
    private byte courseYear;

    @Column(name = "COURSE_TYPE", nullable = false, length = 100)
    private String courseType;

    @Column(name = "COURSE_SUBTYPE", nullable = false, length = 100)
    private String courseSubtype;
}
