package org.unilab.uniplan.major;

import java.util.UUID;

public class MajorNotFoundException extends RuntimeException {
    public MajorNotFoundException(UUID id) {
        super("Could not find major with id " + id);
    }
}
