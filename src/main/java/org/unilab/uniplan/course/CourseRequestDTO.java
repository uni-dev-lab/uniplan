package org.unilab.uniplan.course;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CourseRequestDTO(
    UUID id,
    @NotNull
    UUID majorId,
    @Positive
    @Min(1)
    @Max(20)
    byte courseYear,
    @NotNull
    @Size(max = 100)
    String courseType,
    @NotNull
    @Size(max = 100)
    String courseSubtype
) {}
