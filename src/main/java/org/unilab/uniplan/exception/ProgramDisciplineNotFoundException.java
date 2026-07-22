package org.unilab.uniplan.exception;

import java.util.UUID;

public class ProgramDisciplineNotFoundException extends ResourceNotFoundException {

    public ProgramDisciplineNotFoundException(final UUID id) {
        super(String.format("Program discipline with ID %s not found.", id));
    }
}
