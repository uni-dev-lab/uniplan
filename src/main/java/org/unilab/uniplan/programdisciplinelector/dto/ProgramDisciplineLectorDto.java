package org.unilab.uniplan.programdisciplinelector.dto;

import jakarta.validation.constraints.NotNull;
import org.unilab.uniplan.discipline.Discipline;
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.program.Program;
import org.unilab.uniplan.programdisciplinelector.LectorType;
import org.unilab.uniplan.programdisciplinelector.ProgramDisciplineLectorId;

public record ProgramDisciplineLectorDto(
    ProgramDisciplineLectorId id,
    @NotNull
    LectorRequestDto lectorRequestDto,
    @NotNull
    Program program,
    @NotNull
    Discipline discipline,
    @NotNull
    LectorType lectorType
) {

}
