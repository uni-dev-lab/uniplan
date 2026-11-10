package org.unilab.uniplan.programdiscipline.dto;

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
