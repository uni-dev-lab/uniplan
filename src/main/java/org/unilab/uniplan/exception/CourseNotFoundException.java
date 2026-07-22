package org.unilab.uniplan.exception;

import java.util.UUID;

public class CourseNotFoundException extends ResourceNotFoundException {

    public CourseNotFoundException(final UUID id) {
        super(String.format("Course with ID %s not found.", id));
    }

}
