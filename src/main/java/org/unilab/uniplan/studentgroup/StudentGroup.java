package org.unilab.uniplan.studentgroup;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.unilab.uniplan.common.model.AuditableEntity;
import org.unilab.uniplan.coursegroup.CourseGroup;
import org.unilab.uniplan.student.Student;

@Entity
@Table(name = "STUDENT_GROUP")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroup extends AuditableEntity {

    @EmbeddedId
    private StudentGroupId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID", nullable = false, insertable = false, updatable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", nullable = false, insertable = false, updatable = false)
    private CourseGroup courseGroup;
}
