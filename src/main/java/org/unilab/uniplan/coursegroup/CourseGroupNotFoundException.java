package org.unilab.uniplan.coursegroup;

import java.util.UUID;

public class CourseGroupNotFoundException extends RuntimeException {
    public CourseGroupNotFoundException(UUID id) {
        super("Could not find courseGroup with id " + id);
    }

}