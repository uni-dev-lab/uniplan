package org.unilab.uniplan.building;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.building.dto.BuildingRequestDto;
import org.unilab.uniplan.building.dto.BuildingResponseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.university.University;
import org.unilab.uniplan.university.UniversityService;

@ExtendWith(MockitoExtension.class)
class BuildingWebFacadeTest {

    @Mock
    private BuildingService buildingService;

    @Mock
    private BuildingMapper buildingMapper;

    @Mock
    private UniversityService universityService;

    @InjectMocks
    private BuildingWebFacade buildingWebFacade;

    private UUID buildingId;
    private UUID universityId;
    private Building building;
    private University university;
    private BuildingRequestDto requestDto;
    private BuildingResponseDto responseDto;

    @BeforeEach
    void setUp() {
        buildingId = UUID.randomUUID();
        universityId = UUID.randomUUID();
        building = new Building();
        university = new University();
        requestDto = mock(BuildingRequestDto.class);
        responseDto = mock(BuildingResponseDto.class);
    }

    @Test
    void createBuildingShouldMapResolveUniversityAndSaveBuilding() {
        when(requestDto.universityId()).thenReturn(universityId);
        when(buildingMapper.toEntity(requestDto)).thenReturn(building);
        when(universityService.getById(universityId)).thenReturn(Optional.of(university));

        buildingWebFacade.createBuilding(requestDto);

        final InOrder inOrder = inOrder(buildingMapper, universityService, buildingService);
        inOrder.verify(buildingMapper).toEntity(requestDto);
        inOrder.verify(universityService).getById(universityId);
        inOrder.verify(buildingService).save(building);
        assertEquals(university, building.getUniversity());
    }

    @Test
    void createBuildingShouldThrowIfUniversityDoesNotExist() {
        when(requestDto.universityId()).thenReturn(universityId);
        when(buildingMapper.toEntity(requestDto)).thenReturn(building);
        when(universityService.getById(universityId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> buildingWebFacade.createBuilding(requestDto));

        verify(buildingService, never()).save(any(Building.class));
    }

    @Test
    void getAllBuildingsShouldReturnResponseDtoList() {
        final List<Building> buildings = List.of(building);
        final List<BuildingResponseDto> responseDtos = List.of(responseDto);

        when(buildingService.getAll()).thenReturn(buildings);
        when(buildingMapper.toResponseDtoList(buildings)).thenReturn(responseDtos);

        final List<BuildingResponseDto> result = buildingWebFacade.getAllBuildings();

        assertEquals(responseDtos, result);
        verify(buildingService).getAll();
        verify(buildingMapper).toResponseDtoList(buildings);
    }

    @Test
    void getBuildingByIdShouldReturnResponseDtoIfFound() {
        when(buildingService.getById(buildingId)).thenReturn(Optional.of(building));
        when(buildingMapper.toResponseDto(building)).thenReturn(responseDto);

        final BuildingResponseDto result = buildingWebFacade.getBuildingById(buildingId);

        assertEquals(responseDto, result);
        verify(buildingService).getById(buildingId);
        verify(buildingMapper).toResponseDto(building);
    }

    @Test
    void getBuildingByIdShouldThrowIfBuildingDoesNotExist() {
        when(buildingService.getById(buildingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> buildingWebFacade.getBuildingById(buildingId));

        verify(buildingService).getById(buildingId);
        verify(buildingMapper, never()).toResponseDto(any(Building.class));
    }

    @Test
    void updateBuildingShouldUpdateResolveUniversityAndSaveBuildingIfFound() {
        when(requestDto.universityId()).thenReturn(universityId);
        when(buildingService.getById(buildingId)).thenReturn(Optional.of(building));
        when(universityService.getById(universityId)).thenReturn(Optional.of(university));

        buildingWebFacade.updateBuilding(buildingId, requestDto);

        final InOrder inOrder = inOrder(buildingService, buildingMapper, universityService);
        inOrder.verify(buildingService).getById(buildingId);
        inOrder.verify(buildingMapper).updateEntityFromDto(requestDto, building);
        inOrder.verify(universityService).getById(universityId);
        inOrder.verify(buildingService).save(building);
        assertEquals(university, building.getUniversity());
    }

    @Test
    void updateBuildingShouldThrowIfBuildingDoesNotExist() {
        when(buildingService.getById(buildingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> buildingWebFacade.updateBuilding(buildingId, requestDto));

        verify(buildingService).getById(buildingId);
        verify(buildingMapper, never()).updateEntityFromDto(any(BuildingRequestDto.class), any(Building.class));
        verify(buildingService, never()).save(any(Building.class));
    }

    @Test
    void updateBuildingShouldThrowIfUniversityDoesNotExist() {
        when(requestDto.universityId()).thenReturn(universityId);
        when(buildingService.getById(buildingId)).thenReturn(Optional.of(building));
        when(universityService.getById(universityId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> buildingWebFacade.updateBuilding(buildingId, requestDto));

        verify(buildingService, never()).save(any(Building.class));
    }

    @Test
    void deleteBuildingShouldDeleteBuildingIfFound() {
        when(buildingService.getById(buildingId)).thenReturn(Optional.of(building));

        buildingWebFacade.deleteBuilding(buildingId);

        verify(buildingService).getById(buildingId);
        verify(buildingService).delete(building);
    }

    @Test
    void deleteBuildingShouldThrowIfBuildingDoesNotExist() {
        when(buildingService.getById(buildingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> buildingWebFacade.deleteBuilding(buildingId));

        verify(buildingService).getById(buildingId);
        verify(buildingService, never()).delete(any(Building.class));
    }
}