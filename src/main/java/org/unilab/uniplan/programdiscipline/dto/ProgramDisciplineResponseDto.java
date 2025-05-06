package org.unilab.uniplan.programdiscipline.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.unilab.uniplan.discipline.Discipline;
import org.unilab.uniplan.program.Program;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

public record ProgramDisciplineResponseDto(
    ProgramDisciplineId id,

    @NotNull
    Discipline discipline,

    @NotNull
    Program program,

    short hoursLecture,

    short hoursExercises,

    @Min(value = 1)
    @Max(value = 12)
    byte semesterCount
) {

}
