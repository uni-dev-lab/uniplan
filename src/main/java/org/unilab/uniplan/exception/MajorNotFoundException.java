package org.unilab.uniplan.exception;

import java.util.UUID;

public class MajorNotFoundException extends ResourceNotFoundException {

    public MajorNotFoundException(final UUID id) {
        super("major_not_found", id);
    }

}
