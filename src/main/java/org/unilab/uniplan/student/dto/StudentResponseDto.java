package org.unilab.uniplan.student.dto;

import java.util.UUID;

public record StudentResponseDto(
    UUID id,
    String firstName,
    String lastName,
    String facultyNumber,
    UUID courseId
) {

}
