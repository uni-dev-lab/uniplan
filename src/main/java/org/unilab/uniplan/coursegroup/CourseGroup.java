package org.unilab.uniplan.coursegroup;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COURSE_GROUP")
public class CourseGroup extends BaseEntity {
  
    @ManyToOne
    @JoinColumn(name = "COURSE_ID", nullable = false)
    private Course course;
}
