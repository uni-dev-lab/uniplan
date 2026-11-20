package org.unilab.uniplan.lector;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.exception.FacultyNotFoundException;
import org.unilab.uniplan.faculty.FacultyRepository;
import org.unilab.uniplan.lector.dto.LectorRequestDto;

@Component
@RequiredArgsConstructor
public class LectorValidator {

    private final FacultyRepository facultyRepository;

    public void validate(final LectorRequestDto request) {
        final UUID id = request.facultyId();
        if (!facultyRepository.existsById(id)) {
            throw new FacultyNotFoundException(id);
        }
    }
}
