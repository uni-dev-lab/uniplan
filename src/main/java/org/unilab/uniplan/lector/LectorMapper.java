package org.unilab.uniplan.lector;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

@Mapper
public interface LectorMapper {

    @Mapping(source = "facultyId", target = "faculty.id")
    Lector toEntity(LectorRequestDto lectorRequestDto);

    @Mapping(source = "faculty.id", target = "facultyId")
    LectorResponseDto toResponseDto(final Lector lector);

    List<LectorResponseDto> toResponseDtoList(List<Lector> lectors);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final LectorRequestDto lectorRequestDto, @MappingTarget final Lector lector);
}
