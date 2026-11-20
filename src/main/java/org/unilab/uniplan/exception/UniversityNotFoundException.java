package org.unilab.uniplan.exception;

import java.util.UUID;

public class UniversityNotFoundException extends ResourceNotFoundException {

    public UniversityNotFoundException(final UUID id) {
        super("university_not_found" , id);
    }

}
