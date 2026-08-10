package org.unilab.uniplan.exception;

import java.util.UUID;

public class RoomNotFoundException extends ResourceNotFoundException {

    public RoomNotFoundException(final UUID id) {
        super("room_not_found", id);
    }

}
