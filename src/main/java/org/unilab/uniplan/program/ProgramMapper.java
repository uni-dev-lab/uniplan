package org.unilab.uniplan.program;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.program.dto.ProgramRequestDto;
import org.unilab.uniplan.program.dto.ProgramResponseDto;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

@Mapper
public interface ProgramMapper {

    /*Before it was target= course.id which would just switch the current Course's id
     * instead of switching to a new Course. We create a new Course object that isn't
     * persisted because we haven't set cascade and then on save() we only change at what
     * Course the Program entity is pointing with it's FK column.*/
    @Mapping(target = "course", source = "courseId", qualifiedByName = "courseIdToCourse")
    @Mapping(target = "programDisciplines", ignore = true)
    @Mapping(target = "id", ignore = true)
    Program toEntity(final ProgramRequestDto request);

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "programDisciplinesIds", source = "programDisciplines",
        qualifiedByName = "programDisciplinesToIds")
    ProgramResponseDto toResponseDto(final Program program);

    List<ProgramResponseDto> toResponseDtoList(final List<Program> requests);

    /*Same as above*/
    @Mapping(target = "course", source = "courseId", qualifiedByName = "courseIdToCourse")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "programDisciplines", ignore = true)
    void updateEntityFromDto(final ProgramRequestDto request,
                             @MappingTarget final Program program);

    @Named("programDisciplinesToIds")
    default List<ProgramDisciplineId> programDisciplinesToIds(
        final List<ProgramDiscipline> programDisciplines) {
        return programDisciplines.stream().map(ProgramDiscipline::getId).toList();
    }

    @Named("courseIdToCourse")
    default Course courseIdToCourse(final UUID courseId) {
        final Course course = new Course();
        course.setId(courseId);
        return course;
    }
}
