package org.unilab.uniplan.exception;

import java.util.UUID;

public class UniversityNotFoundException extends ResourceNotFoundException {

    public UniversityNotFoundException(final UUID id) {
        super(String.format("University with ID %s not found.", id));
    }

}
