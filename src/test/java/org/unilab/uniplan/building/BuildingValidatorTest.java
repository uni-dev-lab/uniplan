package org.unilab.uniplan.building;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.building.dto.BuildingRequestDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.university.University;
import org.unilab.uniplan.university.UniversityService;

@ExtendWith(MockitoExtension.class)
class BuildingValidatorTest {

    @Mock
    private UniversityService universityService;

    @InjectMocks
    private BuildingValidator buildingValidator;

    private UUID universityId;
    private BuildingRequestDto requestDto;
    private University university;

    @BeforeEach
    void setUp() {
        universityId = UUID.randomUUID();
        university = new University();
        requestDto = mock(BuildingRequestDto.class);

        when(requestDto.universityId()).thenReturn(universityId);
    }

    @Test
    void validateAndResolveUniversity_shouldReturnUniversity_whenUniversityExists() {
        when(universityService.getById(universityId)).thenReturn(Optional.of(university));

        final University result = buildingValidator.validateAndResolveUniversity(requestDto);

        assertThat(result).isSameAs(university);
        verify(universityService).getById(universityId);
    }

    @Test
    void validateAndResolveUniversity_shouldThrowResourceNotFoundException_whenUniversityDoesNotExist() {
        when(universityService.getById(universityId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                               buildingValidator.validateAndResolveUniversity(requestDto)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(universityService).getById(universityId);
    }
}