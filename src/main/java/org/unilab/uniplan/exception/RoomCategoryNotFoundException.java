package org.unilab.uniplan.exception;


import org.unilab.uniplan.roomcategory.RoomCategoryId;

public class RoomCategoryNotFoundException extends ResourceNotFoundException {

    public RoomCategoryNotFoundException(final RoomCategoryId id) {
        super(String.format("Room category with ID %s not found.", id));
    }

}
