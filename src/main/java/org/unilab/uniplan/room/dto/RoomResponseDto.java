package org.unilab.uniplan.room.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record RoomResponseDto(

    UUID id,

    UUID facultyId,

    String roomNumber
) {

}
