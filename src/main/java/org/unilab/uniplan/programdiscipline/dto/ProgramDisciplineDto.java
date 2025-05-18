package org.unilab.uniplan.programdiscipline.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;
import java.util.UUID;

public record ProgramDisciplineDto(
    ProgramDisciplineId id,

    @NotNull
    UUID disciplineId,

    @NotNull
    UUID programId,

    short hoursLecture,

    short hoursExercises,

    @Min(value = 1)
    @Max(value = 12)
    byte semesterCount
) {

}
