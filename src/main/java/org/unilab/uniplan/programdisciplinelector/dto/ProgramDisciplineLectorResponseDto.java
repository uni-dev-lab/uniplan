package org.unilab.uniplan.programdisciplinelector.dto;

import org.unilab.uniplan.programdisciplinelector.LectorType;
import org.unilab.uniplan.programdisciplinelector.ProgramDisciplineLectorId;
import java.util.UUID;

public record ProgramDisciplineLectorResponseDto(
    ProgramDisciplineLectorId id,
    UUID lectorId,
    UUID programId,
    UUID disciplineId,
    LectorType lectorType
) {

}
