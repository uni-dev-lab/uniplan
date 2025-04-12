package org.unilab.uniplan.faculty;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.university.UniversityMapper;

@Mapper(componentModel = "spring", uses = UniversityMapper.class)
public interface FacultyMapper {

    @Mapping(target = "universityId", source = "university.id")
    @Mapping(target = "facultyName", source = "facultyName")
    @Mapping(target = "location", source = "location")
    FacultyDto toDto(final Faculty faculty);

    @Mapping(target = "university", source = "universityId")
    @Mapping(target = "facultyName", source = "facultyName")
    @Mapping(target = "location", source = "location")
    Faculty toEntity(final FacultyDto facultyDto);

    List<FacultyDto> toDtoList(final List<Faculty> faculties);

    @Mapping(target = "university", source = "universityId")
    @Mapping(target = "facultyName", source = "facultyName")
    @Mapping(target = "location", source = "location")
    void updateEntityFromDto(final FacultyDto facultyDto, @MappingTarget final Faculty faculty);

    Faculty map(final UUID value);
}
