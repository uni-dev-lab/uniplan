package org.unilab.uniplan.room.dto;

import java.util.UUID;

public record RoomResponseDto(

    UUID id,

    UUID facultyId,

    String roomNumber,

    UUID categoryId

) { }
