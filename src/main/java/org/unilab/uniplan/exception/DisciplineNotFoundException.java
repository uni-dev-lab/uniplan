package org.unilab.uniplan.exception;

import java.util.UUID;

public class DisciplineNotFoundException extends ResourceNotFoundException {

    public DisciplineNotFoundException(final UUID id) {
        super(String.format("Discipline with ID %s not found.", id));
    }

}
