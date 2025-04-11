package org.unilab.uniplan.lector;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LectorMapper {

    @Mapping(source = "faculty.id", target = "facultyId")
    LectorDto toDto(Lector lector);

    Lector toEntity(LectorDto lectorDto);
}
