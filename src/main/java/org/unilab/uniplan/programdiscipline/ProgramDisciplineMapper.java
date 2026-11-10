package org.unilab.uniplan.programdiscipline;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineResponseDto;

@Mapper
public interface ProgramDisciplineMapper {

    @Mapping(target = "program", ignore = true)
    @Mapping(target = "discipline", ignore = true)
    @Mapping(target = "id.disciplineId", source = "disciplineId")
    @Mapping(target = "id.programId", source = "programId")
    ProgramDiscipline toEntity(ProgramDisciplineRequestDto request);

    @Mapping(target = "programId", source = "id.programId")
    @Mapping(target = "disciplineId", source = "id.disciplineId")
    ProgramDisciplineResponseDto toResponseDto(final ProgramDiscipline entity);

    List<ProgramDisciplineResponseDto> toResponseDtoList(List<ProgramDiscipline> entityList);

    @Mapping(target = "program", ignore = true)
    @Mapping(target = "discipline", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final ProgramDisciplineRequestDto request,
                             @MappingTarget final ProgramDiscipline entity);
}
