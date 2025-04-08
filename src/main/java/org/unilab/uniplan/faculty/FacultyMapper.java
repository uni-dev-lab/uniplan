package org.unilab.uniplan.faculty;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FacultyMapper {

    @Mapping(source = "university.id", target = "universityId")
    FacultyDto toDto(Faculty faculty);

    @Mapping(source = "universityId", target = "university")
    Faculty toEntity(FacultyDto facultyDto);
}
