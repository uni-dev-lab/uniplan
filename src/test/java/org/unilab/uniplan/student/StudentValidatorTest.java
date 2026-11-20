package org.unilab.uniplan.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.course.CourseRepository;
import org.unilab.uniplan.exception.CourseNotFoundException;
import org.unilab.uniplan.student.dto.StudentRequestDto;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentValidatorTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentValidator studentValidator;

    private UUID courseId;
    private StudentRequestDto requestDto;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        requestDto = new StudentRequestDto("Petar", "Petrov", "2301261005", courseId);
    }

    @Test
    void validate_ShouldPass_WhenCourseExists() {
        when(courseRepository.existsById(courseId)).thenReturn(true);

        assertDoesNotThrow(() -> studentValidator.validate(requestDto));
    }

    @Test
    void validate_shouldThrowCourseNotFoundException_whenCourseDoesNotExist() {
        when(courseRepository.existsById(courseId)).thenReturn(false);

        assertThatThrownBy(() -> studentValidator.validate(requestDto))
            .isInstanceOfSatisfying(CourseNotFoundException.class, ex -> {
                assertThat(ex.getMessageKey()).isEqualTo("course_not_found");
                assertThat(ex.getArgs()).containsExactly(courseId);
            });

        verify(courseRepository).existsById(courseId);
    }
}
