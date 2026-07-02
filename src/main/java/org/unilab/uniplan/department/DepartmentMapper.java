package org.unilab.uniplan.department;

import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.department.dto.DepartmentDto;
import org.unilab.uniplan.department.dto.DepartmentRequestDto;
import org.unilab.uniplan.department.dto.DepartmentResponseDto;
import org.unilab.uniplan.faculty.Faculty;

@Mapper
public interface DepartmentMapper {

    @Mapping(target = "faculty.id", source = "facultyId")
    @Mapping(target = "id", ignore = true)
    Department toEntity(final DepartmentRequestDto requestDto);

    @Mapping(target = "facultyId", source = "faculty.id")
    DepartmentDto toDto(Department department);

    @Mapping(source="faculty.id", target = "facultyId")
    DepartmentResponseDto toResponseDto(Department department);

    List<DepartmentResponseDto> toResponseDtoList(final List<Department> departments);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "facultyId", target = "faculty")
    void updateEntity(DepartmentRequestDto requestDto, @MappingTarget Department department);

    @Mapping(target = "id", source = "facultyId")
    Faculty toFaculty(UUID facultyId);
}