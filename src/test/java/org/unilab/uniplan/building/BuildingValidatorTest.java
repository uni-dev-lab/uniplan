package org.unilab.uniplan.building;

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
import org.unilab.uniplan.building.dto.BuildingRequestDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.university.UniversityRepository;

@ExtendWith(MockitoExtension.class)
class BuildingValidatorTest {

    @Mock
    private UniversityRepository universityRepository;

    @InjectMocks
    private BuildingValidator buildingValidator;

    private UUID universityId;
    private BuildingRequestDto requestDto;

    @BeforeEach
    void setUp() {
        universityId = UUID.randomUUID();
        requestDto = mock(BuildingRequestDto.class);

        when(requestDto.universityId()).thenReturn(universityId);
    }

    @Test
    void validate_shouldPass_whenUniversityExists() {
        when(universityRepository.existsById(universityId)).thenReturn(true);

        assertDoesNotThrow(() -> buildingValidator.validate(requestDto));

        verify(universityRepository).existsById(universityId);
    }

    @Test
    void validate_shouldThrow_whenUniversityDoesNotExist() {
        when(universityRepository.existsById(universityId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                     () -> buildingValidator.validate(requestDto));

        verify(universityRepository).existsById(universityId);
    }
}