package org.unilab.uniplan.university;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UniversityMapper {

    UniversityDto toDto(University university);

    University toEntity(UniversityDto universityDto);
}

