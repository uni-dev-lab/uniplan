package org.unilab.uniplan.discipline.dto;

import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;
import java.util.List;
import java.util.UUID;

public record DisciplineResponseDto(

    UUID id,

    String name,

    String mainLector,

    List<ProgramDisciplineId> programDisciplinesIds
) {

}
