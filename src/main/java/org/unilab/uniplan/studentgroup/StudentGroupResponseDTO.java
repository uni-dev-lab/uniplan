package org.unilab.uniplan.studentgroup;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record StudentGroupResponseDTO(
    @NotNull
    UUID studentId,
    @NotNull
    UUID courseGroupId
) {

}
