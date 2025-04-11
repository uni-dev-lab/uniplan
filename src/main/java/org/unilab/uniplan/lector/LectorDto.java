package org.unilab.uniplan.lector;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record LectorDto(

    UUID id,

    @NotNull(message = "Faculty ID cannot be null")
    UUID facultyId,

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    String email
) {

}
