package org.unilab.uniplan.discipline;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.discipline.dto.DisciplineDto;
import org.unilab.uniplan.discipline.dto.DisciplineRequestDto;
import org.unilab.uniplan.discipline.dto.DisciplineResponseDto;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineMapper;

@Mapper(uses = ProgramDisciplineMapper.class)
public interface DisciplineMapper {

    @Mapping(target = "programDisciplinesIds", source = "programDisciplines", qualifiedByName = "toProgramDisciplineId")
    DisciplineDto toDto(final Discipline discipline);

    @Mapping(target = "programDisciplines", source = "programDisciplinesIds", qualifiedByName = "toProgramDiscipline")
    Discipline toEntity(final DisciplineDto disciplineDto);

    @Mapping(target = "id", ignore = true)
    DisciplineDto toInternalDto(final DisciplineRequestDto disciplineRequestDto);

    DisciplineResponseDto toResponseDto(final DisciplineDto disciplineDto);

    List<DisciplineDto> toDtoList(final List<Discipline> disciplines);

    List<DisciplineResponseDto> toResponseDtoList(final List<DisciplineDto> disciplineDtos);

    @Mapping(target = "name", source = "disciplineDto.name")
    @Mapping(target = "mainLector", source = "disciplineDto.mainLector")
    @Mapping(target = "programDisciplines", ignore = true)
    void updateEntityFromDto(final DisciplineDto disciplineDto,
                             @MappingTarget final Discipline discipline);
}
