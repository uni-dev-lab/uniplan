package org.unilab.uniplan.student;

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
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.unilab.uniplan.common.model.Person;
import org.unilab.uniplan.course.Course;

@Entity
@Table(name = "student")
@SoftDelete(strategy = SoftDeleteType.DELETED, columnName = "is_deleted")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends Person {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "faculty_number", unique = true, nullable = false, length = 100)
    private String facultyNumber;
}