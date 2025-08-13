package org.unilab.uniplan.coursegroup;

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
import org.unilab.uniplan.course.Course;

@Entity
@Table(name = "course_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseGroup extends BaseEntity {
  
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Column(name = "max_group", nullable = false)
    private int maxGroup;
}
