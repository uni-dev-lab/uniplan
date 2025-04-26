package org.unilab.uniplan.faculty;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.faculty.dto.FacultyDto;
import org.unilab.uniplan.faculty.dto.FacultyRequestDto;
import org.unilab.uniplan.faculty.dto.FacultyResponseDto;

@Mapper
public interface FacultyMapper {

    @Mapping(target = "university.id", source = "universityId")
    Faculty toEntity(FacultyDto facultyDto);

    @Mapping(target = "universityId", source = "university.id")
    FacultyDto toDto(Faculty faculty);

    @Mapping(target = "universityId", source = "universityId")
    @Mapping(target = "id", ignore = true)
    FacultyDto toInternalDto(FacultyRequestDto facultyRequestDto);

    @Mapping(target = "universityId", source = "universityId")
    FacultyResponseDto toResponseDto(FacultyDto facultyDto);

    List<FacultyDto> toDtoList(List<Faculty> faculties);

    List<FacultyResponseDto> toResponseDtoList(List<FacultyDto> faculties);

    @Mapping(target = "university.id", source = "facultyDto.universityId")
    @Mapping(target = "facultyName", source = "facultyDto.facultyName")
    @Mapping(target = "location", source = "facultyDto.location")
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(FacultyDto facultyDto, @MappingTarget Faculty faculty);
}
