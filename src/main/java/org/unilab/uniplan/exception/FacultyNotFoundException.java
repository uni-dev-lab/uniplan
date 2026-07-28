package org.unilab.uniplan.exception;

import java.util.UUID;

public class FacultyNotFoundException extends ResourceNotFoundException {

    public FacultyNotFoundException(final UUID id) {
        super("faculty_not_found", id);
    }

}
