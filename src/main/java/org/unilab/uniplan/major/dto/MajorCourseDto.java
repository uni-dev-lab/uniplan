package org.unilab.uniplan.major.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record MajorCourseDto(
    UUID majorId,
    @NotNull
    UUID courseId,
    @NotNull
    UUID facultyId,
    @NotNull
    @Size(max = 200)
    String majorName,
    @Size(max = 100)
    String courseType,
    @NotNull
    @Size(max = 100)
    String courseSubtype
) {

}
