package org.unilab.uniplan.coursegroup.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CourseGroupResponseDto(
    UUID id,
    @NotNull
    UUID courseId,
    @NotNull
    @Size(max = 200)
    String groupName,
    @Positive
    @Max(40)
    int maxGroup
) {

}
