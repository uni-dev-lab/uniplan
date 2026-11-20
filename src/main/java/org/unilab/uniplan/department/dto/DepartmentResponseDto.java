package org.unilab.uniplan.department.dto;

import java.util.UUID;

public record DepartmentResponseDto(

    UUID id,

    UUID facultyId,

    String departmentName
) {

}
