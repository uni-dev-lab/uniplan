package org.unilab.uniplan.studentgroup;

import java.util.UUID;

public class StudentGroupNotFoundException extends RuntimeException {
    public StudentGroupNotFoundException(UUID id) {
        super("Could not find studentGroup with id " + id);
    }

}
