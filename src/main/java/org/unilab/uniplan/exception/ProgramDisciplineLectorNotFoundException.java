package org.unilab.uniplan.exception;

import java.util.UUID;

public class ProgramDisciplineLectorNotFoundException extends ResourceNotFoundException {

    public ProgramDisciplineLectorNotFoundException(final UUID id) {
        super(String.format("Program discipline lector with ID %s not found.", id));
    }
}
