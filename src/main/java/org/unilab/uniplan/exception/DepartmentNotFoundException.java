package org.unilab.uniplan.exception;

import java.util.UUID;

public class DepartmentNotFoundException extends ResourceNotFoundException {

    public DepartmentNotFoundException(final UUID id) {
        super(String.format("Department with ID %s not found.", id));
    }
}
