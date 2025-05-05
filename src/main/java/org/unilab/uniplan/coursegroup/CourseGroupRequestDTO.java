package org.unilab.uniplan.coursegroup;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record CourseGroupRequestDTO(
    @NotNull
    UUID courseId,
    @NotNull
    String groupName,
    @Positive
    int maxGroup
) {}
