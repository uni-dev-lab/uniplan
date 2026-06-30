package org.unilab.uniplan.lector;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyRepository;

@ExtendWith(MockitoExtension.class)
class LectorValidatorTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private LectorValidator lectorValidator;

    private UUID facultyId;
    private Lector lector;

    @BeforeEach
    void setUp() {
        facultyId = UUID.randomUUID();

        final Faculty faculty = new Faculty();
        faculty.setId(facultyId);

        lector = new Lector();
        lector.setFaculty(faculty);
    }

    @Test
    void validate_shouldPass_whenFacultyExists() {
        when(facultyRepository.existsById(facultyId)).thenReturn(true);

        assertThatCode(() -> lectorValidator.validate(lector))
            .doesNotThrowAnyException();
    }

    @Test
    void validate_shouldThrowResourceNotFoundException_whenFacultyDoesNotExist() {
        when(facultyRepository.existsById(facultyId)).thenReturn(false);

        assertThatThrownBy(() -> lectorValidator.validate(lector))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(facultyId.toString());
    }
}
