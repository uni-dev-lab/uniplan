package org.unilab.uniplan.studentgroup;

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
import org.unilab.uniplan.common.model.BaseEntity;
import org.unilab.uniplan.coursegroup.CourseGroup;
import org.unilab.uniplan.student.Student;
import java.util.List;

@Entity
@Table(name = "STUDENT_GROUP")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroup extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID", nullable = false)
    private List<Student> studentList;

    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private CourseGroup courseGroup;
}
