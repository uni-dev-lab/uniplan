package org.unilab.uniplan.program.dto;

import jakarta.validation.constraints.NotNull;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;
import java.util.List;
import java.util.UUID;

public record ProgramDto(
    UUID id,
    @NotNull(message = "Course cannot be null")
    Course course,
    @NotNull
    List<ProgramDiscipline> programDisciplines
) {

}
