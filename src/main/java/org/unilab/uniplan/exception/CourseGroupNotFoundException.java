package org.unilab.uniplan.exception;

import java.util.UUID;

public class CourseGroupNotFoundException extends ResourceNotFoundException {

    public CourseGroupNotFoundException(final UUID id) {
        super("course_group_not_found", id);
    }

}
