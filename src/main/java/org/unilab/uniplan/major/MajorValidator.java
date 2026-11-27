package org.unilab.uniplan.major;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.exception.FacultyNotFoundException;
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
            throw new FacultyNotFoundException(facultyId);
        }
    }
}