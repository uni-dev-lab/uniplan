package org.unilab.uniplan.major.dto;

import java.util.UUID;

public record MajorResponseDto(
    UUID id,
    UUID facultyId,
    String majorName
) {

}
