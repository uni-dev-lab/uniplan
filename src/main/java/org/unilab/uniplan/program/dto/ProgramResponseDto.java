package org.unilab.uniplan.program.dto;

import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;
import java.util.List;
import java.util.UUID;

public record ProgramResponseDto(
    UUID id,
    UUID courseId,
    List<ProgramDisciplineId> programDisciplinesIds
) {

}
