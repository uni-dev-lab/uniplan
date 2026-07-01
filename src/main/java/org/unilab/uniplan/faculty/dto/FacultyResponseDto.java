package org.unilab.uniplan.faculty.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record FacultyResponseDto(

    UUID id,

    UUID universityId,

    String facultyName,

    String location
) {}
