package org.unilab.uniplan.programdisciplinelector.dto;

import jakarta.validation.constraints.NotNull;
import org.unilab.uniplan.programdisciplinelector.LectorType;
import java.util.UUID;

public record ProgramDisciplineLectorRequestDto(
    @NotNull
    UUID lectorId,
    @NotNull
    UUID programId,
    @NotNull
    UUID disciplineId,
    @NotNull
    LectorType lectorType
) {

}
