package org.unilab.uniplan.exception;

import java.util.UUID;

public class ProgramNotFoundException extends ResourceNotFoundException {

    public ProgramNotFoundException(final UUID id) {
        super(String.format("Program with ID %s not found.", id));
    }

}
