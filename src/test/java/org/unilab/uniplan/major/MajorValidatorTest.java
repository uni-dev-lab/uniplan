package org.unilab.uniplan.major;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
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
import org.unilab.uniplan.major.dto.MajorRequestDto;

@ExtendWith(MockitoExtension.class)
class MajorValidatorTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private MajorValidator majorValidator;

    private UUID facultyId;
    private MajorRequestDto requestDto;

    @BeforeEach
    void setUp() {
        facultyId = UUID.randomUUID();
        requestDto = mock(MajorRequestDto.class);

        when(requestDto.facultyId()).thenReturn(facultyId);
    }

    @Test
    void validate_shouldPass_whenFacultyExists() {
        when(facultyRepository.existsById(facultyId)).thenReturn(true);

        assertDoesNotThrow(() -> majorValidator.validate(requestDto));

        verify(facultyRepository).existsById(facultyId);
    }

    @Test
    void validate_shouldThrow_whenFacultyDoesNotExist() {
        when(facultyRepository.existsById(facultyId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> majorValidator.validate(requestDto));

        verify(facultyRepository).existsById(facultyId);
    }
}