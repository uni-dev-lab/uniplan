package org.unilab.uniplan.roomcategory.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RoomCategoryRequestDto(

    @NotNull(message = "Room ID cannot be null")
    UUID roomId,

    @NotNull(message = "Category ID cannot be null")
    UUID categoryId
) {

}
