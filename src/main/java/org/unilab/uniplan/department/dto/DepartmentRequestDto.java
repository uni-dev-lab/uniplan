package org.unilab.uniplan.department.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record DepartmentRequestDto(

    @NotNull(message = "Faculty ID cannot be null")
    UUID facultyId,

    @NotNull(message = "Department name cannot be null")
    @Size(min = 1, max = 200, message = "Department name must be between 1 and 200 characters")
    String departmentName
) {

}
