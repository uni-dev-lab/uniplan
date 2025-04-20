package org.unilab.uniplan.course;

import java.util.UUID;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(UUID id) {
        super("Course with id " + id + " doesn't exists");
    }

}
