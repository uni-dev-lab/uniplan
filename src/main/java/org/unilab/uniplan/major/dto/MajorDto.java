package org.unilab.uniplan.major.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record MajorDto(
    UUID id,
    @NotNull
    UUID facultyId,
    @NotNull
    @Size(max = 200)
    String majorName
) {

}