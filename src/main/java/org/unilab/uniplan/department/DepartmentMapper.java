package org.unilab.uniplan.department;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.department.dto.DepartmentDto;
import org.unilab.uniplan.department.dto.DepartmentRequestDto;
import org.unilab.uniplan.department.dto.DepartmentResponseDto;

@Mapper
public interface DepartmentMapper {

    @Mapping(target = "faculty.id", source = "facultyId")
    Department toEntity(final DepartmentDto departmentDto);

    @Mapping(target = "facultyId", source = "faculty.id")
    DepartmentDto toDto(final Department department);

    @Mapping(target = "facultyId", source = "facultyId")
    @Mapping(target = "id", ignore = true)
    DepartmentDto toInternalDto(final DepartmentRequestDto departmentRequestDto);

    @Mapping(target = "facultyId", source = "facultyId")
    DepartmentResponseDto toResponseDto(final DepartmentDto departmentDto);

    List<DepartmentDto> toDtoList(final List<Department> departments);

    List<DepartmentResponseDto> toResponseDtoList(final List<DepartmentDto> departments);
    
    @Mapping(target = "faculty.id", source = "departmentDto.facultyId")
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(final DepartmentDto departmentDto,
                             @MappingTarget final Department department);
}