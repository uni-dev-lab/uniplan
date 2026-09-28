package org.unilab.uniplan.exception;

import java.util.UUID;

public class StudentNotFoundException extends ResourceNotFoundException {

    public StudentNotFoundException(final UUID id) {
        super("student_not_found" , id);
    }

}
