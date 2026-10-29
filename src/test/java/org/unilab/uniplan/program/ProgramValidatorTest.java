package org.unilab.uniplan.program;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.course.CourseRepository;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.program.dto.ProgramRequestDto;

@ExtendWith(MockitoExtension.class)
class ProgramValidatorTest {

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private ProgramValidator programValidator;

    private UUID programId;
    private UUID courseId;
    private ProgramRequestDto request;

    @BeforeEach
    void setUp() {
        programId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        request = new ProgramRequestDto(courseId);
    }

    @Test
    void validateForCreate_shouldNotThrow_whenValid() {
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(programRepository.existsByCourse_Id(courseId)).thenReturn(false);

        assertDoesNotThrow(() -> programValidator.validateForCreate(request));
    }

    @Test
    void validateForCreate_shouldThrowNotFound_whenCourseDoesNotExist() {
        when(courseRepository.existsById(courseId)).thenReturn(false);

        assertThatThrownBy(() -> programValidator.validateForCreate(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(courseId.toString());

        verify(programRepository, never()).existsByCourse_Id(any());
    }

    @Test
    void validateForCreate_shouldThrowConflict_whenProgramWithCourseIdExists() {
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(programRepository.existsByCourse_Id(courseId)).thenReturn(true);

        assertThatThrownBy(() -> programValidator.validateForCreate(request))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining(courseId.toString())
            .satisfies(exception -> {
                final ResponseStatusException ex = (ResponseStatusException) exception;
                assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            });
    }

    @Test
    void validateForUpdate_shouldNotThrow_whenValid() {
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(programRepository.existsByCourse_IdAndIdNot(courseId, programId))
            .thenReturn(false);

        assertDoesNotThrow(() -> programValidator.validateForUpdate(programId, request));
    }

    @Test
    void validateForUpdate_shouldThrowNotFound_whenCourseDoesNotExist() {
        when(courseRepository.existsById(courseId)).thenReturn(false);

        assertThatThrownBy(() -> programValidator.validateForUpdate(programId, request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(courseId.toString());

        verify(programRepository, never()).existsByCourse_IdAndIdNot(any(), any());
    }

    @Test
    void validateForUpdate_shouldThrowConflict_whenOtherProgramExistsWithCourseId() {
        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(programRepository.existsByCourse_IdAndIdNot(courseId, programId))
            .thenReturn(true);

        assertThatThrownBy(() -> programValidator.validateForUpdate(programId, request))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining(courseId.toString())
            .satisfies(exception -> {
                final ResponseStatusException ex = (ResponseStatusException) exception;
                assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            });
    }
}
