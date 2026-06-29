package org.unilab.uniplan.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.course.CourseRepository;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentValidatorTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentValidator studentValidator;

    private Student student;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        Course course = new Course();
        course.setId(courseId);
        student = new Student();
        student.setCourse(course);
    }

    @Test
    void validateShouldPassWhenCourseExists() {
        when(courseRepository.existsById(courseId)).thenReturn(true);

        assertDoesNotThrow(() -> studentValidator.validate(student));
    }

    @Test
    void validateShouldThrowWhenCourseNotFound() {
        when(courseRepository.existsById(courseId)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> studentValidator.validate(student));

        assertTrue(exception.getMessage().contains(courseId.toString()));
    }
}
