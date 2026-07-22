package org.unilab.uniplan.exception;

import java.util.UUID;

public class RoomNotFoundException extends ResourceNotFoundException {

    public RoomNotFoundException(final UUID id) {
        super(String.format("Room with ID %s not found.", id));
    }

}
