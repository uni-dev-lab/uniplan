package org.unilab.uniplan.major.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record MajorRequestDto(
    @NotNull(message = "Faculty id cannot be null")
    UUID facultyId,
    @NotBlank(message = "Major name cannot be null")
    @Size(max = 200, message = "Major name must be at most 200 characters")
    String majorName
) {

}
