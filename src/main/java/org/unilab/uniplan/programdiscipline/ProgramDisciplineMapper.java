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

    @Mapping(source = "discipline.id", target = "disciplineId")
    @Mapping(source = "program.id", target = "programId")
    ProgramDisciplineDto toDto(ProgramDiscipline programDiscipline);

    @Mapping(source = "disciplineId", target = "discipline.id")
    @Mapping(source = "programId", target = "program.id")
    ProgramDiscipline toEntity(ProgramDisciplineDto programDisciplineDto);

    @Mapping(target = "id", ignore = true)
    ProgramDisciplineDto toInternalDto(final ProgramDisciplineRequestDto programDisciplineRequestDto);

    ProgramDisciplineResponseDto toResponseDto(final ProgramDisciplineDto programDisciplineDto);

    List<ProgramDisciplineDto> toDtos(List<ProgramDiscipline> programDisciplines);

    List<ProgramDisciplineResponseDto> toResponseDtoList(List<ProgramDisciplineDto> programDisciplineDtos);

    @Mapping(target = "program.id", source = "programDisciplineDto.programId")
    @Mapping(target = "discipline.id", source = "programDisciplineDto.disciplineId")
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final ProgramDisciplineDto programDisciplineDto,
                             @MappingTarget final ProgramDiscipline programDiscipline);
}
