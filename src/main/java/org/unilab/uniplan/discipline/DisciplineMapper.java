package org.unilab.uniplan.discipline;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.discipline.dto.DisciplineDto;
import org.unilab.uniplan.discipline.dto.DisciplineRequestDto;
import org.unilab.uniplan.discipline.dto.DisciplineResponseDto;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DisciplineMapper {

    @Mapping(target = "programDisciplineList", source = "programDisciplines")
    DisciplineDto toDto(final Discipline discipline);

    @Mapping(target = "programDisciplines", source = "programDisciplineList")
    Discipline toEntity(final DisciplineDto disciplineDto);

    DisciplineDto toInternalDto(final DisciplineRequestDto disciplineRequestDto);

    DisciplineResponseDto toResponseDto(final DisciplineDto disciplineDto);

    List<DisciplineDto> toDtoList(final List<Discipline> disciplines);

    List<DisciplineResponseDto> toResponseDtoList(final List<DisciplineDto> disciplineDtos);

    @Mapping(target = "name", source = "categoryDto.name")
    @Mapping(target = "mainLector", source = "categoryDto.mainLector")
    void updateEntityFromDto(final DisciplineDto disciplineDto,
                             @MappingTarget final Discipline discipline);
}
