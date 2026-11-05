package org.unilab.uniplan.lector;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.FacultyRepository;
import org.unilab.uniplan.lector.dto.LectorRequestDto;

@ExtendWith(MockitoExtension.class)
class LectorValidatorTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private LectorValidator lectorValidator;

    @Test
    void validate_shouldPass_whenFacultyExists() {
        final LectorRequestDto request = createRequest();
        when(facultyRepository.existsById(request.facultyId())).thenReturn(true);

        assertDoesNotThrow(() -> lectorValidator.validate(request));

        verify(facultyRepository).existsById(request.facultyId());
    }

    @Test
    void validate_shouldThrowResourceNotFoundException_whenFacultyDoesNotExist() {
        final LectorRequestDto request = createRequest();

        assertThatThrownBy(() -> lectorValidator.validate(request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(request.facultyId().toString());

        verify(facultyRepository).existsById(request.facultyId());
    }

    private LectorRequestDto createRequest() {
        return new LectorRequestDto(UUID.randomUUID(),
                                    "jane.doe@example.com",
                                    "Jane",
                                    "Doe");
    }
}
