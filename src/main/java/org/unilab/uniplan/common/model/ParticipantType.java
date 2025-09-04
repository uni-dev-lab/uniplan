package org.unilab.uniplan.common.model;

import lombok.Getter;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.coursegroup.CourseGroup;
import org.unilab.uniplan.lector.Lector;
import org.unilab.uniplan.student.Student;

@Getter
public enum ParticipantType {
    STUDENT(Student.class),
    LECTOR(Lector.class),
    GROUP(CourseGroup.class),
    COURSE(Course.class);

    private final Class<? extends BaseEntity> entityClass;

    ParticipantType(Class<? extends BaseEntity> entityClass) {
        this.entityClass = entityClass;
    }

}
