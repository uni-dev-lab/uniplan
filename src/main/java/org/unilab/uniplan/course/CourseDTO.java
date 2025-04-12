package org.unilab.uniplan.course;

import java.util.UUID;

public record CourseDTO(
    UUID id,
    UUID majorId,
    byte courseYear,
    String courseType,
    String courseSubtype
) {}