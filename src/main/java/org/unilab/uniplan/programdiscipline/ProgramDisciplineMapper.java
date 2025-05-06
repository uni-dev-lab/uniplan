package org.unilab.uniplan.programdiscipline;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineResponseDto;
import java.util.List;

@Mapper
public interface ProgramDisciplineMapper {

    ProgramDisciplineDto toDto(ProgramDiscipline programDiscipline);

    ProgramDiscipline toEntity(ProgramDisciplineDto programDisciplineDto);

    void updateEntity(ProgramDisciplineDto programDisciplineDto, @MappingTarget ProgramDiscipline programDiscipline);

    @Mapping(target = "id", ignore = true)
    ProgramDisciplineDto toInternalDto(final ProgramDisciplineRequestDto programDisciplineRequestDto);

    ProgramDisciplineResponseDto toResponseDto(final ProgramDisciplineDto programDisciplineDto);

    List<ProgramDisciplineDto> toDtos(List<ProgramDiscipline> programDisciplines);

    List<ProgramDisciplineResponseDto> toResponseDtoList(List<ProgramDisciplineDto> programDisciplineDtos);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final ProgramDisciplineDto programDisciplineDto, @MappingTarget final ProgramDiscipline programDiscipline);
}
