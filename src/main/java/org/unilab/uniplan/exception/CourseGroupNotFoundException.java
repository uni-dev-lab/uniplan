package org.unilab.uniplan.exception;

import java.util.UUID;

public class CourseGroupNotFoundException extends ResourceNotFoundException {

    public CourseGroupNotFoundException(final UUID id) {
        super(String.format("Course group with ID %s not found.", id));
    }

}
