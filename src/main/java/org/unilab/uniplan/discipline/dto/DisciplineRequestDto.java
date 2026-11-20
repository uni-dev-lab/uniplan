package org.unilab.uniplan.discipline.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DisciplineRequestDto(
    @NotBlank(message = "Discipline name must not be blank")
    @Size(max = 100, message = "Discipline name must be at most 100 characters")
    String name,

    @NotBlank(message = "Main lector must not be blank")
    @Size(max = 200, message = "Main lector name must be at most 200 characters")
    String mainLector
) {

}
