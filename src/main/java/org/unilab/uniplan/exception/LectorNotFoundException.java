package org.unilab.uniplan.exception;

import java.util.UUID;

public class LectorNotFoundException extends ResourceNotFoundException {

    public LectorNotFoundException(final UUID id) {
        super("lector_not_found", id);
    }

}
