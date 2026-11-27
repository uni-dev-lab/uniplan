package org.unilab.uniplan.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.course.CourseRepository;
import org.unilab.uniplan.exception.CourseNotFoundException;
import org.unilab.uniplan.student.dto.StudentRequestDto;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StudentValidator{
    private final CourseRepository courseRepository;

    public void validate(final StudentRequestDto request) {
        UUID id = request.courseId();
        if (!courseRepository.existsById(id)){
            throw new CourseNotFoundException(id);
        }
    }
}
