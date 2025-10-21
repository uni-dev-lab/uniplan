package org.unilab.uniplan.student.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record StudentCourseMajorDto(
    UUID studentId,
    @NotBlank
    @Size(max = 100)
    String firstName,
    @NotBlank
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
    Byte courseYear,
    @NotNull
    UUID majorId,
    @NotBlank
    @Size(max = 200)
    String majorName
) {

}
