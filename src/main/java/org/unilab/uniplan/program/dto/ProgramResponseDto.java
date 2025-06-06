package org.unilab.uniplan.program.dto;

import jakarta.validation.constraints.NotNull;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;
import java.util.List;
import java.util.UUID;

public record ProgramResponseDto(
    UUID id,
    @NotNull(message = "Course cannot be null")
    UUID courseId,
    @NotNull
    List<ProgramDisciplineId> programDisciplinesIds
) {

}
