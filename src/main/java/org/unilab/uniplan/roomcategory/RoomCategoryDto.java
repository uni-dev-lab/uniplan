package org.unilab.uniplan.roomcategory;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RoomCategoryDto(

    @NotNull
    UUID roomId,

    @NotNull
    UUID categoryId

) {

}
