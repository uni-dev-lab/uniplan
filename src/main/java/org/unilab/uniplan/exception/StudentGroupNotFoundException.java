package org.unilab.uniplan.exception;

import org.unilab.uniplan.studentgroup.StudentGroupId;

public class StudentGroupNotFoundException extends ResourceNotFoundException {

    public StudentGroupNotFoundException(final StudentGroupId id) {
        super("student_group_not_found", id);
    }

}
