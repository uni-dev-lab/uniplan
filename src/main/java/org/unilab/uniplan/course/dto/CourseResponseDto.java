package org.unilab.uniplan.course.dto;

import java.util.UUID;

public record CourseResponseDto(
    UUID id,
    UUID majorId,
    byte courseYear,
    String courseType,
    String courseSubtype
) {

}
