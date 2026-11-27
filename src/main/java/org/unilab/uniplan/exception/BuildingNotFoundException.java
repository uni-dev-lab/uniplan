package org.unilab.uniplan.exception;

import java.util.UUID;

public class BuildingNotFoundException extends ResourceNotFoundException {

    public BuildingNotFoundException(final UUID id) {
        super("building_not_found", id);
    }
}
