package org.unilab.uniplan.lector;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.FacultyRepository;
import org.unilab.uniplan.lector.dto.LectorRequestDto;

import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.FACULTY_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class LectorValidator {

    private final FacultyRepository facultyRepository;

    public void validateForCreate(final LectorRequestDto request) {
        validateFacultyExists(request.facultyId());
    }

    public void validateForUpdate(final LectorRequestDto request) {
        validateFacultyExists(request.facultyId());
    }

    private void validateFacultyExists(final UUID facultyId) {
        if (!facultyRepository.existsById(facultyId)) {
            throw new ResourceNotFoundException(
                FACULTY_NOT_FOUND.getMessage(String.valueOf(facultyId))
            );
        }
    }
}