package org.unilab.uniplan.department;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.department.dto.DepartmentRequestDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.FacultyRepository;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.FACULTY_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class DepartmentValidator {

    private final FacultyRepository facultyRepository;

    public void validate(DepartmentRequestDto request) {
        final UUID id = request.facultyId();
        if (!facultyRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                FACULTY_NOT_FOUND.getMessage(String.valueOf(id)));
        }
    }

}
