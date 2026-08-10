package org.unilab.uniplan.exception;

import java.util.UUID;

public class CourseNotFoundException extends ResourceNotFoundException {

    public CourseNotFoundException(final UUID id) {
        super("course_not_found", id);
    }

}
