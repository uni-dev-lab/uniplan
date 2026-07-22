package org.unilab.uniplan.exception;

import java.util.UUID;

public class StudentNotFoundException extends ResourceNotFoundException {

    public StudentNotFoundException(final UUID id) {
        super(String.format("Student with ID %s not found.", id));
    }
    
}
