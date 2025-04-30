package org.unilab.uniplan.coursegroup;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CourseGroupResponseDTO(
    UUID id,
    @NotNull
    UUID courseId,
    @NotNull
    String groupName,
    @NotNull
    int maxGroup
) {}
