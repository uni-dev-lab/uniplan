package org.unilab.uniplan.program.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ProgramRequestDto(
    @NotNull(message = "Course cannot be null")
    UUID courseId
) {

}
