package org.unilab.uniplan.program;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.program.dto.ProgramDto;
import org.unilab.uniplan.program.dto.ProgramRequestDto;
import org.unilab.uniplan.program.dto.ProgramResponseDto;
import org.unilab.uniplan.programdiscipline.ProgramDisciplineMapper;

@Mapper(uses = ProgramDisciplineMapper.class)
public interface ProgramMapper {

    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "programDisciplinesIds", target = "programDisciplines", qualifiedByName = "toProgramDiscipline")
    Program toEntity(final ProgramDto programDto);

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "programDisciplines", target = "programDisciplinesIds", qualifiedByName = "toProgramDisciplineId")
    ProgramDto toDto(final Program program);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "programDisciplinesId", target = "programDisciplinesIds")
    ProgramDto toInternalDto(final ProgramRequestDto requestDto);

    List<ProgramDto> toDtoList(final List<Program> programs);

    List<ProgramResponseDto> toResponseDtoList(final List<ProgramDto> programDtos);

    @Mapping(target = "course.id", source = "programDto.courseId")
    @Mapping(target = "programDisciplines", ignore = true)
    void updateEntityFromDto(final ProgramDto programDto,
                             @MappingTarget final Program program);
}
