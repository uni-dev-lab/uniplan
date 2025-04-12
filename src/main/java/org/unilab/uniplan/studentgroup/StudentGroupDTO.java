package org.unilab.uniplan.studentgroup;

import java.util.UUID;

public record StudentGroupDTO(
    UUID id,
    UUID studentId,
    UUID courseGroupId
) {}
