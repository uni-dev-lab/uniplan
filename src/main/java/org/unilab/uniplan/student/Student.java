package org.unilab.uniplan.student;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class Student extends Person{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURSE_ID", nullable = false)
    private UUID courseId;
    @Column(name = "STUDENT_NAME", nullable = false)
    @Size(max = 255, message = "Student names must not exceed 255 characters")
    private String studentName;

    @Column(name = "FACULTY_NUMBEr", nullable = false, unique = true)
    @Size(max = 512, message = "Faculty number must not exceed 512 characters")
    private String facNumber;
}
