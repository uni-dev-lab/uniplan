package org.unilab.uniplan.lector;

import static org.unilab.uniplan.utils.ErrorConstants.FACULTY_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.FacultyRepository;
import org.unilab.uniplan.lector.dto.LectorRequestDto;

@Component
@RequiredArgsConstructor
public class LectorValidator {

    private final FacultyRepository facultyRepository;

    public void validate(final LectorRequestDto request) {
        final UUID id = request.facultyId();
        if (!facultyRepository.existsById(id)) {
            throw new ResourceNotFoundException(FACULTY_NOT_FOUND.getMessage(id.toString()));
        }
    }
}
