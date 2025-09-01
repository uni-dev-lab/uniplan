package org.unilab.uniplan.student.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record StudentCourseMajorDto(
    UUID studentId,
    @NotNull
    @Size(max = 100)
    String firstName,
    @NotNull
    @Size(max = 100)
    String lastName,
    @Size(max = 40)
    String facultyNumber,
    @NotNull
    UUID courseId,
    @NotNull
    String courseType,
    @NotNull
    String courseSubType,
    @NotNull
    byte courseYear,
    @NotNull
    UUID majorId,
    @NotNull
    String majorName
) {

}
