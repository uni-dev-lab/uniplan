package org.unilab.uniplan.lector;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.lector.dto.LectorDto;
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

@Mapper
public interface LectorMapper {

    @Mapping(source = "facultyId", target = "faculty.id")
    @Mapping(target = "id", ignore = true)
    Lector toEntity(LectorRequestDto requestDto);

    @Mapping(source = "faculty.id", target = "facultyId")
    LectorDto toDto(Lector lector);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "facultyId", target = "faculty")
    void updateEntity(LectorRequestDto requestDto, @MappingTarget Lector lector);

    @Mapping(source = "faculty.id", target = "facultyId")
    LectorResponseDto toResponseDto(Lector lector);

    List<LectorResponseDto> toResponseDtoList(List<Lector> lectors);

    @Mapping(target = "id", source = "facultyId")
    Faculty toFaculty(UUID facultyId);

}
