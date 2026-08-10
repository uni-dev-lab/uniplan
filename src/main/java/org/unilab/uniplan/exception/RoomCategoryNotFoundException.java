package org.unilab.uniplan.exception;


import org.unilab.uniplan.roomcategory.RoomCategoryId;

public class RoomCategoryNotFoundException extends ResourceNotFoundException {

    public RoomCategoryNotFoundException(final RoomCategoryId id) {
        super("room_category_not_found", id);
    }

}
