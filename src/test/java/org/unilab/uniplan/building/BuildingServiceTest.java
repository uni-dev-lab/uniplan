package org.unilab.uniplan.building;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        assertEquals(Optional.of(building), result);
        verify(buildingRepository).findById(buildingId);
    }

    @Test
    void getAll_shouldReturnBuildings_whenBuildingsExist() {
        final List<Building> buildings = List.of(building);
        when(buildingRepository.findAllWithUniversity()).thenReturn(buildings);

        final List<Building> result = buildingService.getAll();

        assertEquals(buildings, result);
        verify(buildingRepository).findAllWithUniversity();
    }

    @Test
    void delete_shouldDeleteBuilding_whenCalled() {
        buildingService.delete(building);

        verify(buildingRepository).delete(building);
    }
}