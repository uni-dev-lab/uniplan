package org.unilab.uniplan.programdisciplinelector.dto;

import jakarta.validation.constraints.NotNull;
import org.unilab.uniplan.programdisciplinelector.LectorType;
import java.util.UUID;

public record ProgramDisciplineLectorRequestDto(
    @NotNull(message = "Lector ID cannot be null")
    UUID lectorId,
    @NotNull(message = "Program ID cannot be null")
    UUID programId,
    @NotNull(message = "Discipline ID cannot be null")
    UUID disciplineId,
    @NotNull(message = "Lector Type cannot be null")
    LectorType lectorType
) {

}
