package org.unilab.uniplan.programdisciplinelector;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorResponseDto;
import java.util.List;

@Mapper
public interface ProgramDisciplineLectorMapper {

    @Mapping(target = "lectorRequestDto", source = "lector")
    ProgramDisciplineLectorDto toDto(ProgramDisciplineLector programDisciplineLector);

    @Mapping(target = "lector", source = "lectorRequestDto")
    ProgramDisciplineLector toEntity(ProgramDisciplineLectorDto programDisciplineLectorDto);

    @Mapping(target = "id", ignore = true)
    ProgramDisciplineLectorDto toInternalDto(final ProgramDisciplineLectorRequestDto requestDto);

    ProgramDisciplineLectorResponseDto toResponseDto(final ProgramDisciplineLectorDto dto);

    List<ProgramDisciplineLectorDto> toDtos(List<ProgramDisciplineLector> programDisciplineLectorList);

    List<ProgramDisciplineLectorResponseDto> toResponseDtoList(List<ProgramDisciplineLectorDto> programDisciplineLectorDtoList);

    @Mapping(target = "lector", source = "lectorRequestDto")
    void updateEntityFromDto(final ProgramDisciplineLectorDto dto, @MappingTarget final ProgramDisciplineLector entity);
}
