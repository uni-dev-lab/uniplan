package org.unilab.uniplan.roomcategory.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RoomCategoryResponseDto(

    UUID roomId,

    UUID categoryId
) {

}
