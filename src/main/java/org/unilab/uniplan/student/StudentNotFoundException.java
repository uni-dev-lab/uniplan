package org.unilab.uniplan.student;

import java.util.UUID;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(UUID id) {
        super("Student with id " + id + " doesn't exists");
    }
}
