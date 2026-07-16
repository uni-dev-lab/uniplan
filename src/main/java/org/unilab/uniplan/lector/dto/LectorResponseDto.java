package org.unilab.uniplan.lector.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record LectorResponseDto(

    UUID id,
    UUID facultyId,
    String email,
    String firstName,
    String lastName
) {

}
