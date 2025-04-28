package org.unilab.uniplan.faculty.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record FacultyResponseDto(

    UUID id,

    @NotNull(message = "University ID cannot be null")
    UUID universityId,

    @NotNull(message = "Faculty name cannot be null")
    @Size(min = 1, max = 200, message = "Faculty name must be between 1 and 200 characters")
    String facultyName,

    @NotNull(message = "Location cannot be null")
    @Size(max = 500, message = "Location must be less than or equal to 500 characters")
    String location
) {

}
