package org.unilab.uniplan.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.common.model.Validator;
import org.unilab.uniplan.course.CourseRepository;
import org.unilab.uniplan.exception.ResourceNotFoundException;

import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.COURSE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class StudentValidator implements Validator<Student> {
    private final CourseRepository courseRepository;

    @Override
    public void validate(final Student entity) {
        UUID id = entity.getCourse().getId();
        if (!courseRepository.existsById(id)){
            throw new ResourceNotFoundException(COURSE_NOT_FOUND.getMessage(id.toString()));
        }
    }
}
