package org.unilab.uniplan.exception;

import java.util.UUID;

public class DisciplineNotFoundException extends ResourceNotFoundException {

    public DisciplineNotFoundException(final UUID id) {
        super("discipline_not_found", id);
    }

}
