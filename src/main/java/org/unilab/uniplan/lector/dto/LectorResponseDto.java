package org.unilab.uniplan.lector.dto;

import java.util.UUID;

public record LectorResponseDto(

    UUID id,

    UUID facultyId,

    String email,

    String firstName,

    String lastName
) {

}
