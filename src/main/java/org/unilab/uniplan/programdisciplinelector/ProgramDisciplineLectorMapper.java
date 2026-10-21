package org.unilab.uniplan.programdisciplinelector;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorResponseDto;

@Mapper
public interface ProgramDisciplineLectorMapper {

    /*Mapping directly to id using the dto's fields to create a new id object
     * ignoring the discipline, lector and program on mapping since they are
     * insertable = false and updatable = false and Hibernate would ignore them on insert*/
    @Mapping(target = "discipline", ignore = true)
    @Mapping(target = "lector", ignore = true)
    @Mapping(target = "program", ignore = true)
    @Mapping(target = "id", expression = "java(new ProgramDisciplineLectorId(request.lectorId(),"
                                         + " request.programId(),"
                                         + " request.disciplineId()))")
    ProgramDisciplineLector toEntity(ProgramDisciplineLectorRequestDto request);

    /*After creating relationships are still null. Until they are fetched they would be null
     * and that would put nulls in the Create response*/
    @Mapping(target = "lectorId", source = "id.lectorId")
    @Mapping(target = "programId", source = "id.programId")
    @Mapping(target = "disciplineId", source = "id.disciplineId")
    ProgramDisciplineLectorResponseDto toResponseDto(final ProgramDisciplineLector programDisciplineLector);

    List<ProgramDisciplineLectorResponseDto> toResponseDtoList(List<ProgramDisciplineLector> programDisciplineLectorList);

    /*Ignoring id, lector program and discipline since changing them would also
     * mean changing the ids connected to the object which would change the id of our entity */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lector", ignore = true)
    @Mapping(target = "program", ignore = true)
    @Mapping(target = "discipline", ignore = true)
    void updateEntityFromDto(final ProgramDisciplineLectorRequestDto request,
                             @MappingTarget final ProgramDisciplineLector entity);
}