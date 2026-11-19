package org.unilab.uniplan.building;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

@ExtendWith(MockitoExtension.class)
class BuildingWebFacadeTest {

    @Mock
    private BuildingService buildingService;

    @Mock
    private BuildingMapper buildingMapper;

    @Mock
    private BuildingValidator buildingValidator;

    @InjectMocks
    private BuildingWebFacade buildingWebFacade;

    private UUID buildingId;
    private Building building;
    private University university;
    private BuildingRequestDto requestDto;
    private BuildingResponseDto responseDto;

    @BeforeEach
    void setUp() {
        buildingId = UUID.randomUUID();
        building = new Building();
        university = new University();
        requestDto = mock(BuildingRequestDto.class);
        responseDto = mock(BuildingResponseDto.class);
    }

    @Test
    void createBuilding_shouldSaveBuilding_whenUniversityExists() {
        when(buildingValidator.validateAndResolveUniversity(requestDto)).thenReturn(university);
        when(buildingMapper.toEntity(requestDto)).thenReturn(building);

        buildingWebFacade.createBuilding(requestDto);

        final InOrder inOrder = inOrder(buildingValidator, buildingMapper, buildingService);

        inOrder.verify(buildingValidator).validateAndResolveUniversity(requestDto);
        inOrder.verify(buildingMapper).toEntity(requestDto);
        inOrder.verify(buildingService).save(building);

        assertEquals(university, building.getUniversity());
    }

    @Test
    void createBuilding_shouldThrowException_whenUniversityDoesNotExist() {
        when(buildingValidator.validateAndResolveUniversity(requestDto))
            .thenThrow(new ResourceNotFoundException("University not found"));

        assertThatThrownBy(() ->
                               buildingWebFacade.createBuilding(requestDto)
        ).isInstanceOf(ResourceNotFoundException.class);

        verify(buildingValidator).validateAndResolveUniversity(requestDto);
        verify(buildingMapper, never()).toEntity(any(BuildingRequestDto.class));
        verify(buildingService, never()).save(any(Building.class));
    }

    @Test
    void getAllBuildings_shouldReturnResponseDtoList_whenBuildingsExist() {
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
    void getBuildingById_shouldReturnResponseDto_whenBuildingExists() {
        when(buildingService.getById(buildingId)).thenReturn(Optional.of(building));
        when(buildingMapper.toResponseDto(building)).thenReturn(responseDto);

        final BuildingResponseDto result = buildingWebFacade.getBuildingById(buildingId);

        assertEquals(responseDto, result);
        verify(buildingService).getById(buildingId);
        verify(buildingMapper).toResponseDto(building);
    }

    @Test
    void getBuildingById_shouldThrowException_whenBuildingDoesNotExist() {
        when(buildingService.getById(buildingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> buildingWebFacade.getBuildingById(buildingId));

        verify(buildingService).getById(buildingId);
        verify(buildingMapper, never()).toResponseDto(any(Building.class));
    }

    @Test
    void updateBuilding_shouldUpdateBuilding_whenBuildingAndUniversityExist() {
        when(buildingValidator.validateAndResolveUniversity(requestDto)).thenReturn(university);
        when(buildingService.getById(buildingId)).thenReturn(Optional.of(building));

        buildingWebFacade.updateBuilding(buildingId, requestDto);

        final InOrder inOrder = inOrder(buildingValidator,
                                        buildingService,
                                        buildingMapper);

        inOrder.verify(buildingValidator).validateAndResolveUniversity(requestDto);
        inOrder.verify(buildingService).getById(buildingId);
        inOrder.verify(buildingMapper).updateEntityFromDto(requestDto, building);
        inOrder.verify(buildingService).save(building);

        assertEquals(university, building.getUniversity());
    }

    @Test
    void updateBuilding_shouldThrowException_whenBuildingDoesNotExist() {
        when(buildingValidator.validateAndResolveUniversity(requestDto)).thenReturn(university);
        when(buildingService.getById(buildingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> buildingWebFacade.updateBuilding(buildingId, requestDto));

        verify(buildingValidator).validateAndResolveUniversity(requestDto);
        verify(buildingService).getById(buildingId);
        verify(buildingMapper, never()).updateEntityFromDto(any(BuildingRequestDto.class),
                                                            any(Building.class));
        verify(buildingService, never()).save(any(Building.class));
    }

    @Test
    void updateBuilding_shouldThrowException_whenUniversityDoesNotExist() {
        when(buildingValidator.validateAndResolveUniversity(requestDto))
            .thenThrow(new ResourceNotFoundException("University not found"));

        assertThrows(ResourceNotFoundException.class,
                     () -> buildingWebFacade.updateBuilding(buildingId, requestDto));

        verify(buildingValidator).validateAndResolveUniversity(requestDto);
        verify(buildingService, never()).getById(buildingId);
        verify(buildingService, never()).save(any(Building.class));
    }

    @Test
    void deleteBuilding_shouldDeleteBuilding_whenBuildingExists() {
        when(buildingService.getById(buildingId)).thenReturn(Optional.of(building));

        buildingWebFacade.deleteBuilding(buildingId);

        verify(buildingService).getById(buildingId);
        verify(buildingService).delete(building);
    }

    @Test
    void deleteBuilding_shouldThrowException_whenBuildingDoesNotExist() {
        when(buildingService.getById(buildingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> buildingWebFacade.deleteBuilding(buildingId));

        verify(buildingService).getById(buildingId);
        verify(buildingService, never()).delete(any(Building.class));
    }
}