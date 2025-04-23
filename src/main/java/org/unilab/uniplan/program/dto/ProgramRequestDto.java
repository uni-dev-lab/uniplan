package org.unilab.uniplan.program.dto;

import jakarta.validation.constraints.NotNull;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;
import java.util.List;

public record ProgramRequestDto(
    @NotNull(message = "Course cannot be null")
    Course course,
    @NotNull
    List<ProgramDiscipline> programDisciplines
) {

}
