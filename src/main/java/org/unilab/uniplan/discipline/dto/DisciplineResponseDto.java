package org.unilab.uniplan.discipline.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;
import java.util.List;
import java.util.UUID;

public record DisciplineResponseDto(

    UUID id,

    @NotBlank(message = "Discipline name must not be blank")
    @Size(max = 100, message = "Discipline name must be at most 255 characters")
    String name,

    @NotBlank(message = "Main lector must not be blank")
    @Size(max = 200, message = "Main lector name must be at most 255 characters")
    String mainLector,

    @NotNull(message = "Program disciplines must not be null")
    @Size(min = 1, message = "At least one program discipline is required")
    List<ProgramDisciplineId> programDisciplinesIds
) {

}
