package org.unilab.uniplan.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.unilab.uniplan.common.model.BaseEntity;

@Entity
@Table(name = "COURSE")
public class Course extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "MAJOR_ID", nullable = false)
    private Major major;

    @Column(name = "COURSE_YEAR", nullable = false)
    @Min(1)
    @Max(20)
    private int courseYear;

    @Column(name = "COURSE_TYPE", nullable = false, length = 255)
    private String courseType;

    @Column(name = "COURSE_SUBTYPE", nullable = false, length = 255)
    private String courseSubtype;
}
