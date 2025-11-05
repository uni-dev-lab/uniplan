package org.unilab.uniplan.programdiscipline;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineResponseDto;

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

    @Mapping(target = "program.id", ignore = true)
    @Mapping(target = "discipline.id", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final ProgramDisciplineDto programDisciplineDto,
                             @MappingTarget final ProgramDiscipline programDiscipline);

    ProgramDisciplineId toProgramDisciplineId(UUID disciplineId, UUID programId);

    @Named("toProgramDisciplineId")
    @Mapping(target = "programId", source = "discipline.id")
    @Mapping(target = "disciplineId", source = "discipline.id")
    ProgramDisciplineId toProgramDisciplineId(ProgramDiscipline programDiscipline);

    @Named("toProgramDiscipline")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hoursLecture", ignore = true)
    @Mapping(target = "hoursExercises", ignore = true)
    @Mapping(target = "semesterCount", ignore = true)
    @Mapping(target = "program.id", source = "programId")
    @Mapping(target = "discipline.id", source = "disciplineId")
    ProgramDiscipline toProgramDiscipline(ProgramDisciplineId programDisciplineId);
}
