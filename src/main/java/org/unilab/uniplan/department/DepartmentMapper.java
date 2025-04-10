package org.unilab.uniplan.department;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(source = "faculty.id", target = "facultyId")
    DepartmentDto toDto(Department department);

    Department toEntity(DepartmentDto departmentDto);
}
