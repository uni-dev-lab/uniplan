package org.unilab.uniplan.course;

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
import org.unilab.uniplan.course.dto.CourseRequestDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.major.MajorRepository;

@ExtendWith(MockitoExtension.class)
class CourseValidatorTest {

    @Mock
    private MajorRepository majorRepository;

    @InjectMocks
    private CourseValidator courseValidator;

    private UUID majorId;
    private CourseRequestDto requestDto;

    @BeforeEach
    void setUp() {
        majorId = UUID.randomUUID();
        requestDto = mock(CourseRequestDto.class);

        when(requestDto.majorId()).thenReturn(majorId);
    }

    @Test
    void validate_shouldPass_whenMajorExists() {
        when(majorRepository.existsById(majorId)).thenReturn(true);

        assertDoesNotThrow(() -> courseValidator.validate(requestDto));

        verify(majorRepository).existsById(majorId);
    }

    @Test
    void validate_shouldThrow_whenMajorDoesNotExist() {
        when(majorRepository.existsById(majorId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> courseValidator.validate(requestDto));

        verify(majorRepository).existsById(majorId);
    }
}