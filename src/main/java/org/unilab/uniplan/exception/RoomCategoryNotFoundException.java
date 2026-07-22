package org.unilab.uniplan.exception;

import java.util.UUID;

public class RoomCategoryNotFoundException extends ResourceNotFoundException {

    public RoomCategoryNotFoundException(final UUID id) {
        super(String.format("Room category with ID %s not found.", id));
    }

}
