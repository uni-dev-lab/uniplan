package org.unilab.uniplan.department;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.unilab.uniplan.faculty.FacultyMapper;

@Mapper(componentModel = "spring", uses = FacultyMapper.class)
public interface DepartmentMapper {

    @Mapping(target = "facultyId", source = "faculty.id")
    DepartmentDto toDto(final Department department);

    @Mapping(target = "faculty", source = "facultyId")
    Department toEntity(final DepartmentDto departmentDto);

    List<DepartmentDto> toDtoList(final List<Department> departments);

    @Mapping(target = "faculty", source = "facultyId")
    @Mapping(target = "departmentName", source = "departmentName")
    void updateEntityFromDto(final DepartmentDto departmentDto,
                             @MappingTarget final Department department);

}
