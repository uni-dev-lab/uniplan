package org.unilab.uniplan.course.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CourseRequestDto(
    @NotNull(message = "Major ID cannot be null")
    UUID majorId,
    @Positive
    @Min(value = 1, message = "Course year must be at least 1")
    @Max(value = 20, message = "Course year must be at least 1")
    byte courseYear,
    @NotBlank(message = "Course type is required")
    @Max(value = 100, message = "Course type must be at most 100 characters")
    String courseType,
    @NotBlank(message = "Course subtype cannot be null")
    @Max(value = 100, message = "Course subtype must be at most 100 characters")
    String courseSubtype
) {

}
