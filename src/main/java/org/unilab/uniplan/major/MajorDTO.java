package org.unilab.uniplan.major;

import java.util.UUID;

public record MajorDTO(
    UUID id,
    UUID facultyId,
    String majorName
) {}