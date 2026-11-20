package org.unilab.uniplan.department;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.department.dto.DepartmentRequestDto;
import org.unilab.uniplan.exception.FacultyNotFoundException;
import org.unilab.uniplan.faculty.FacultyRepository;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class DepartmentValidator {

    private final FacultyRepository facultyRepository;

    public void validate(DepartmentRequestDto request) {
        final UUID id = request.facultyId();
        if (!facultyRepository.existsById(id)) {
            throw new FacultyNotFoundException(id);
        }
    }

}
