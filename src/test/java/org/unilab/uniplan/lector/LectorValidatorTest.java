package org.unilab.uniplan.lector;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
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

    private UUID facultyId;
    private LectorRequestDto requestDto;

    @BeforeEach
    void setUp() {
        facultyId = UUID.randomUUID();
        requestDto = new LectorRequestDto(facultyId, "ivan@example.com", "Ivan", "Ivanov");
    }

    @Test
    void testValidateForCreateShouldPassWhenFacultyExists() {
        when(facultyRepository.existsById(facultyId)).thenReturn(true);

        assertDoesNotThrow(() -> lectorValidator.validateForCreate(requestDto));

        verify(facultyRepository).existsById(facultyId);
    }

    @Test
    void testValidateForCreateShouldThrowWhenFacultyDoesNotExist() {
        when(facultyRepository.existsById(facultyId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> lectorValidator.validateForCreate(requestDto));

        verify(facultyRepository).existsById(facultyId);
    }

    @Test
    void testValidateForUpdateShouldPassWhenFacultyExists() {
        when(facultyRepository.existsById(facultyId)).thenReturn(true);

        assertDoesNotThrow(() -> lectorValidator.validateForUpdate(requestDto));

        verify(facultyRepository).existsById(facultyId);
    }

    @Test
    void testValidateForUpdateShouldThrowWhenFacultyDoesNotExist() {
        when(facultyRepository.existsById(facultyId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> lectorValidator.validateForUpdate(requestDto));

        verify(facultyRepository).existsById(facultyId);
    }
}