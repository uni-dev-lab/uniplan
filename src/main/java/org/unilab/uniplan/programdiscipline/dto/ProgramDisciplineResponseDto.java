package org.unilab.uniplan.programdiscipline.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;
import java.util.UUID;

public record ProgramDisciplineResponseDto(
    ProgramDisciplineId id,

    UUID disciplineId,

    UUID programId,

    short hoursLecture,

    short hoursExercises,

    byte semesterCount
) {

}
