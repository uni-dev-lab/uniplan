package org.unilab.uniplan.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.course.CourseRepository;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.student.dto.StudentRequestDto;

import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.COURSE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class StudentValidator{
    private final CourseRepository courseRepository;

    public void validate(final StudentRequestDto request) {
        UUID id = request.courseId();
        if (!courseRepository.existsById(id)){
            throw new ResourceNotFoundException(COURSE_NOT_FOUND.getMessage(id.toString()));
        }
    }
}
