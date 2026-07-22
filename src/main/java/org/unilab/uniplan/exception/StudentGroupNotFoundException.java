package org.unilab.uniplan.exception;

import org.unilab.uniplan.studentgroup.StudentGroupId;
import java.util.UUID;

public class StudentGroupNotFoundException extends ResourceNotFoundException {

    public StudentGroupNotFoundException(final StudentGroupId id) {
        super(String.format("Student group with ID %s not found.", id));
    }

}
