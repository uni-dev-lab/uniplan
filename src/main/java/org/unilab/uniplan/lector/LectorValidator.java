package org.unilab.uniplan.lector;

import static org.unilab.uniplan.utils.ErrorConstants.FACULTY_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.common.model.Validator;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.FacultyRepository;

@Component
@RequiredArgsConstructor
public class LectorValidator implements Validator<Lector> {

    private final FacultyRepository facultyRepository;

    @Override
    public void validate(final Lector entity) {
        final UUID id = entity.getFaculty().getId();
        if (!facultyRepository.existsById(id)) {
            throw new ResourceNotFoundException(FACULTY_NOT_FOUND.getMessage(id.toString()));
        }
    }
}
