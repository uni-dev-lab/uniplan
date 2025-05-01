package org.unilab.uniplan.room.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record RoomDto(

    UUID id,

    @NotNull(message = "Faculty ID cannot be null")
    UUID facultyId,

    @NotNull(message = "Room number cannot be null")
    @Size(max = 50, message = "Room number cannot exceed 50 characters")
    String roomNumber
) {

}
