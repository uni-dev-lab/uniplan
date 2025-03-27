package org.unilab.uniplan.coursegroup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.unilab.uniplan.course.Course;

@Entity
@Table(name = "COURSE_GROUP")
public class CourseGroup {
    @ManyToOne
    @JoinColumn(name = "COURSE_ID", nullable = false)
    private Course course;
    
    @Column(name = "GROUP_NAME", nullable = false)
    private String groupName;

    @Column(name = "MAX_GROUP", nullable = false)
    private int maxGroup;
}
