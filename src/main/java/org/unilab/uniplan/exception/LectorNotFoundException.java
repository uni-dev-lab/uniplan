package org.unilab.uniplan.exception;

import java.util.UUID;

public class LectorNotFoundException extends ResourceNotFoundException {

    public LectorNotFoundException(final UUID id) {
        super(String.format("Lector with ID %s not found.", id));
    }

}
