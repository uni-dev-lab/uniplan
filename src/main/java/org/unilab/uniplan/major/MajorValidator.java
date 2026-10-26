package org.unilab.uniplan.major;

import static org.unilab.uniplan.utils.ErrorConstants.FACULTY_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.FacultyRepository;
import org.unilab.uniplan.major.dto.MajorRequestDto;

@Component
@RequiredArgsConstructor
public class MajorValidator {

    private final FacultyRepository facultyRepository;

    public void validate(final MajorRequestDto requestDto) {
        validateFacultyExists(requestDto.facultyId());
    }

    private void validateFacultyExists(final UUID facultyId) {
        if (!facultyRepository.existsById(facultyId)) {
            throw new ResourceNotFoundException(
                FACULTY_NOT_FOUND.getMessage(String.valueOf(facultyId))
            );
        }
    }
}