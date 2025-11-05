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
    Faculty toEntity(final FacultyDto facultyDto);

    @Mapping(target = "universityId", source = "university.id")
    FacultyDto toDto(final Faculty faculty);

    @Mapping(target = "id", ignore = true)
    FacultyDto toInternalDto(final FacultyRequestDto facultyRequestDto);

    FacultyResponseDto toResponseDto(final FacultyDto facultyDto);

    List<FacultyDto> toDtoList(final List<Faculty> faculties);

    List<FacultyResponseDto> toResponseDtoList(final List<FacultyDto> faculties);

    @Mapping(target = "university.id", source = "facultyDto.universityId")
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final FacultyDto facultyDto, @MappingTarget final Faculty faculty);
}
