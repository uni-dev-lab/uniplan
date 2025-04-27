package org.unilab.uniplan.category.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CategoryDto(

    UUID id,

    @NotNull(message = "Room type cannot be null")
    @Size(min = 1, max = 50, message = "Room type must be between 1 and 50 characters")
    String roomType,

    @Positive
    short capacity
) {

}
