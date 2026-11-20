package org.unilab.uniplan.discipline;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.unilab.uniplan.discipline.dto.DisciplineRequestDto;
import org.unilab.uniplan.discipline.dto.DisciplineResponseDto;
import org.unilab.uniplan.programdiscipline.ProgramDiscipline;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineId;

@Mapper
public interface DisciplineMapper {
    @Mapping(target = "programDisciplines", ignore = true)
    @Mapping(target = "id", ignore = true)
    Discipline toEntity(final DisciplineRequestDto request);

    @Mapping(target = "programDisciplinesIds",
        source = "programDisciplines",
        qualifiedByName = "programDisciplinesToIds")
    DisciplineResponseDto toResponseDto(final Discipline discipline);

    List<DisciplineResponseDto> toResponseDtoList(final List<Discipline> disciplines);

    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "mainLector", source = "request.mainLector")
    @Mapping(target = "programDisciplines", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final DisciplineRequestDto request,
                             @MappingTarget final Discipline discipline);

    @Named("programDisciplinesToIds")
    default List<ProgramDisciplineId> programDisciplinesToIds(
        final List<ProgramDiscipline> programDisciplines) {
        return programDisciplines.stream().map(ProgramDiscipline::getId).toList();
    }
}
