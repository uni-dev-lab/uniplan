package org.unilab.uniplan.discipline;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DisciplineMapper {

    DisciplineDto toDto(Discipline discipline);

    @Mapping(target = "programDisciplines", source = "programDisciplineList")
    Discipline toEntity(DisciplineDto disciplineDto);
}
