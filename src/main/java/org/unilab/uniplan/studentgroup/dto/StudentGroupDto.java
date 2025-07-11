package org.unilab.uniplan.studentgroup.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record StudentGroupDto(
    @NotNull
    UUID studentId,
    @NotNull
    UUID courseGroupId
) {

}
