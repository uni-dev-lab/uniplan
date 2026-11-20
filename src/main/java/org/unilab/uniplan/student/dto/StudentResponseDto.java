package org.unilab.uniplan.student.dto;

import java.util.UUID;

public record StudentResponseDto(
    UUID id,
    String name,
    String facultyNumber,
    UUID majorId,
    String majorName,
    String courseType,
    String courseSubtype,
    Byte courseYear
) {

}
