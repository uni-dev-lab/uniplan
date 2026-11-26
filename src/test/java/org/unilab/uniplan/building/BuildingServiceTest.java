package org.unilab.uniplan.building;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BuildingServiceTest {

    @Mock
    private BuildingRepository buildingRepository;

    @InjectMocks
    private BuildingService buildingService;

    private Building building;
    private UUID buildingId;

    @BeforeEach
    void setUp() {
        buildingId = UUID.randomUUID();
        building = new Building();
    }

    @Test
    void save_shouldSaveBuilding_whenCalled() {
        buildingService.save(building);

        verify(buildingRepository).save(building);
    }

    @Test
    void getById_shouldReturnBuilding_whenBuildingExists() {
        when(buildingRepository.findById(buildingId)).thenReturn(Optional.of(building));

        final Optional<Building> result = buildingService.getById(buildingId);

        assertThat(result).isEqualTo(Optional.of(building));
        verify(buildingRepository).findById(buildingId);
    }

    @Test
    void getAll_shouldReturnBuildings_whenBuildingsExist() {
        final List<Building> buildings = List.of(building);
        when(buildingRepository.findAllWithUniversity()).thenReturn(buildings);

        final List<Building> result = buildingService.getAll();

        assertThat(result).isEqualTo(buildings);
        verify(buildingRepository).findAllWithUniversity();
    }

    @Test
    void delete_shouldDeleteBuilding_whenCalled() {
        buildingService.delete(building);

        verify(buildingRepository).delete(building);
    }

    @Test
    void hasAssignedRooms_shouldReturnTrue_whenBuildingHasRooms() {
        when(buildingRepository.hasAssignedRooms(buildingId)).thenReturn(true);

        final boolean result = buildingService.hasAssignedRooms(buildingId);

        assertThat(result).isTrue();
        verify(buildingRepository).hasAssignedRooms(buildingId);
    }
}