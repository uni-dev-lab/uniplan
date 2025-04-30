package org.unilab.uniplan.major;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record MajorResponseDTO(
    UUID id,
    @NotNull
    UUID facultyId,
    @NotNull
    @Size(max = 200)
    String majorName
) {}
