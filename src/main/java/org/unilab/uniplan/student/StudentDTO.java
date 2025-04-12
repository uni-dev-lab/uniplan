package org.unilab.uniplan.student;

import java.util.UUID;

public record StudentDTO(
    UUID id,
    String firstName,
    String lastName,
    String facultyNumber,
    UUID courseId
) {}
