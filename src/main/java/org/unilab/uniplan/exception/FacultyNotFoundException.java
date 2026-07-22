package org.unilab.uniplan.exception;

import java.util.UUID;

public class FacultyNotFoundException extends ResourceNotFoundException {

    public FacultyNotFoundException(final UUID id) {
        super(String.format("Faculty with ID %s not found.", id));
    }

}
