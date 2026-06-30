package org.unilab.uniplan.student.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record StudentRequestDto(
    @NotBlank
    @Size(max = 100)
    String firstName,
    @NotBlank
    @Size(max = 100)
    String lastName,
    @NotBlank
    @Size(max = 40)
    String facultyNumber,
    @NotNull
    UUID courseId
) {

}
