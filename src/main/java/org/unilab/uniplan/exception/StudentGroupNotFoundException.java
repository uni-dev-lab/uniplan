package org.unilab.uniplan.exception;

import java.util.UUID;

public class StudentGroupNotFoundException extends ResourceNotFoundException {

    public StudentGroupNotFoundException(final UUID id) {
        super(String.format("Student group with ID %s not found.", id));
    }

}
