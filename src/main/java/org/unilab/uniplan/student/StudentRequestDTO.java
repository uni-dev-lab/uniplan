package org.unilab.uniplan.student;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record StudentRequestDTO (
    @NotNull
    @Size(max = 100)
    String firstName,
    @NotNull
    @Size(max = 100)
    String lastName,
    String facultyNumber,
    UUID courseId
) {}
