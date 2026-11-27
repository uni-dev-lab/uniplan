package org.unilab.uniplan.building;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.building.dto.BuildingRequestDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.university.University;
import org.unilab.uniplan.university.UniversityService;

@ExtendWith(MockitoExtension.class)
class BuildingValidatorTest {

    @Mock
    private UniversityService universityService;

    @Mock
    private BuildingService buildingService;

    @InjectMocks
    private BuildingValidator buildingValidator;

    private UUID universityId;
    private BuildingRequestDto requestDto;
    private University university;

    @BeforeEach
    void setUp() {
        universityId = UUID.randomUUID();
        university = new University();
        requestDto = new BuildingRequestDto(
            "Main Building",
            "123 Campus Road",
            universityId
        );
    }

    @Test
    void validateAndResolveUniversity_shouldReturnUniversity_whenUniversityExists() {
        when(universityService.getById(universityId)).thenReturn(Optional.of(university));

        final University result = buildingValidator.validateAndResolveUniversity(requestDto);

        assertThat(result).isSameAs(university);
        verify(universityService).getById(universityId);
    }

    @Test
    void validateAndResolveUniversity_shouldThrowNotFound_whenUniversityMissing() {
        when(universityService.getById(universityId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> buildingValidator.validateAndResolveUniversity(requestDto))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(universityService).getById(universityId);
    }


    @Test
    void validateCanDelete_shouldThrowConflict_whenBuildingHasRooms() {
        final UUID buildingId = UUID.randomUUID();
        when(buildingService.hasAssignedRooms(buildingId)).thenReturn(true);

        assertThatThrownBy(() -> buildingValidator.validateCanDelete(buildingId))
            .isInstanceOfSatisfying(ResponseStatusException.class, ex ->
                assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT)
            );

        verify(buildingService).hasAssignedRooms(buildingId);
    }

    @Test
    void validateCanDelete_shouldSucceed_whenBuildingHasNoRooms() {
        final UUID buildingId = UUID.randomUUID();
        when(buildingService.hasAssignedRooms(buildingId)).thenReturn(false);

        buildingValidator.validateCanDelete(buildingId);

        verify(buildingService).hasAssignedRooms(buildingId);
    }

}