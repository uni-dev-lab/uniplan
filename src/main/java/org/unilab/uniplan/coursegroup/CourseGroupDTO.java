package org.unilab.uniplan.coursegroup;

import java.util.UUID;

public record CourseGroupDTO(
    UUID id,
    UUID courseId,
    String groupName,
    int maxGroup
) {}
