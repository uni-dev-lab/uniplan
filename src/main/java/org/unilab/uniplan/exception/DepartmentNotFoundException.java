package org.unilab.uniplan.exception;

import java.util.UUID;

public class DepartmentNotFoundException extends ResourceNotFoundException {

    public DepartmentNotFoundException(final UUID id) {
        super("department_not_found", id);
    }
}