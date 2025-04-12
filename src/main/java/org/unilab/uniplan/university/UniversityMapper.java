package org.unilab.uniplan.university;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.faculty.FacultyMapper;

@Mapper(componentModel = "spring", uses = {FacultyMapper.class})
public interface UniversityMapper {

    @Mapping(source = "uniName", target = "uniName")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "establishedYear", target = "establishedYear")
    @Mapping(source = "accreditation", target = "accreditation")
    @Mapping(source = "website", target = "website")
    UniversityDto toDto(University university);

    @Mapping(source = "uniName", target = "uniName")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "establishedYear", target = "establishedYear")
    @Mapping(source = "accreditation", target = "accreditation")
    @Mapping(source = "website", target = "website")
    University toEntity(UniversityDto universityDto);

    List<UniversityDto> toDtoList(List<University> universities);

    @Mapping(source = "uniName", target = "uniName")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "establishedYear", target = "establishedYear")
    @Mapping(source = "accreditation", target = "accreditation")
    @Mapping(source = "website", target = "website")
    void updateEntityFromDto(UniversityDto universityDto, @MappingTarget University university);

    University map(UUID value);
}
