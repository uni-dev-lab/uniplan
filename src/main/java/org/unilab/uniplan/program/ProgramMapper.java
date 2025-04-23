package org.unilab.uniplan.program;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.program.dto.ProgramDto;
import org.unilab.uniplan.program.dto.ProgramRequestDto;
import java.util.List;

@Mapper
public interface ProgramMapper {
    Program toEntity(final ProgramDto programDto);

    ProgramDto toDto(final Program program);
    @Mapping(target = "id", ignore = true)
    ProgramDto toInternalDto(final ProgramRequestDto requestDto);

    List<ProgramDto> toDtoList(final List<Program> programs);

    @Mapping(target = "course", source = "programDto.course")
    @Mapping(target = "programDisciplines", source = "programDto.programDisciplines")
    void updateEntityFromDto(final ProgramDto programDto,
                             @MappingTarget final Program program);
}
