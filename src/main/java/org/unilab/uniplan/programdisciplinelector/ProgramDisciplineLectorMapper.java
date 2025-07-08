package org.unilab.uniplan.programdisciplinelector;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorResponseDto;

@Mapper
public interface ProgramDisciplineLectorMapper {

    @Mapping(target = "disciplineId", source = "discipline.id")
    @Mapping(target = "lectorId", source = "lector.id")
    @Mapping(target = "programId", source = "program.id")
    ProgramDisciplineLectorDto toDto(ProgramDisciplineLector programDisciplineLector);

    @Mapping(target = "discipline.id", source = "disciplineId")
    @Mapping(target = "lector.id", source = "lectorId")
    @Mapping(target = "program.id", source = "programId")
    ProgramDisciplineLector toEntity(ProgramDisciplineLectorDto programDisciplineLectorDto);

    @Mapping(target = "id", ignore = true)
    ProgramDisciplineLectorDto toInternalDto(final ProgramDisciplineLectorRequestDto requestDto);

    ProgramDisciplineLectorResponseDto toResponseDto(final ProgramDisciplineLectorDto dto);

    List<ProgramDisciplineLectorDto> toDtos(List<ProgramDisciplineLector> programDisciplineLectorList);

    List<ProgramDisciplineLectorResponseDto> toResponseDtoList(List<ProgramDisciplineLectorDto> programDisciplineLectorDtoList);

    @Mapping(target = "lector.id", source = "dto.lectorId")
    @Mapping(target = "discipline.id", source = "dto.disciplineId")
    @Mapping(target = "program.id", source = "dto.programId")
    void updateEntityFromDto(final ProgramDisciplineLectorDto dto, @MappingTarget final ProgramDisciplineLector entity);

    ProgramDisciplineLectorId toProgramDisciplineLectorId(UUID lectorId,
                                                          UUID programId,
                                                          UUID disciplineId);
}
