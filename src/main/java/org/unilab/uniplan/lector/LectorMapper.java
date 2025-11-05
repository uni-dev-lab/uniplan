package org.unilab.uniplan.lector;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.lector.dto.LectorDto;
import org.unilab.uniplan.lector.dto.LectorRequestDto;
import org.unilab.uniplan.lector.dto.LectorResponseDto;

@Mapper
public interface LectorMapper {

    @Mapping(source = "faculty.id", target = "facultyId")
    LectorDto toDto(final Lector lector);

    @Mapping(source = "facultyId", target = "faculty.id")
    Lector toEntity(final LectorDto lectorDto);

    @Mapping(target = "id", ignore = true)
    LectorDto toInternalDto(final LectorRequestDto lectorRequestDto);

    LectorResponseDto toResponseDto(final LectorDto lectorDto);

    List<LectorDto> toDtos(final List<Lector> lectors);

    List<LectorResponseDto> toResponseDtoList(final List<LectorDto> lectorDtos);

    @Mapping(target = "email", source = "lectorDto.email")
    @Mapping(target = "firstName", source = "lectorDto.firstName")
    @Mapping(target = "lastName", source = "lectorDto.lastName")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "faculty.id", ignore = true)
    void updateEntityFromDto(final LectorDto lectorDto, @MappingTarget final Lector lector);
}
