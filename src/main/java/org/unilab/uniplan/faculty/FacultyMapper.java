package org.unilab.uniplan.faculty;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.faculty.dto.FacultyRequestDto;
import org.unilab.uniplan.faculty.dto.FacultyResponseDto;

@Mapper
public interface FacultyMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "university.id", source = "universityId")
    Faculty toEntity(FacultyRequestDto facultyDto);

    @Mapping(target = "universityId", source = "university.id")
    FacultyResponseDto toResponseDto(Faculty faculty);

    List<FacultyResponseDto> toResponseDtoList(List<Faculty> faculties);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "university.id", source = "facultyDto.universityId")
    void updateEntity(FacultyRequestDto facultyDto, @MappingTarget Faculty faculty);
}