package org.unilab.uniplan.category.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CategoryRequestDto(

    @NotNull(message = "Room type cannot be null")
    @Size(min = 1, max = 50, message = "Room type must be between 1 and 50 characters")
    String roomType,

    @Positive
    short capacity,

    @Size(min = 1, max = 100, message = "Description must be between 1 and 100 characters")
     String description
) {

}
