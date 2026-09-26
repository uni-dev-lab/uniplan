package org.unilab.uniplan.building;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.building.dto.BuildingRequestDto;
import org.unilab.uniplan.building.dto.BuildingResponseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.university.University;
import org.unilab.uniplan.university.UniversityService;
import java.util.List;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.BUILDING_NOT_FOUND;
import static org.unilab.uniplan.utils.ErrorConstants.UNIVERSITY_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class BuildingWebFacade {
    private final BuildingService buildingService;
    private final BuildingMapper buildingMapper;
    private final UniversityService universityService;
    private final BuildingValidator buildingValidator;

    @Transactional
    public void createBuilding(final BuildingRequestDto request) {
        buildingValidator.validate(request);

        final Building building = buildingMapper.toEntity(request);
        building.setUniversity(getUniversityOrThrow(request.universityId()));
        buildingService.save(building);

        log.info("created building with ID: {}", building.getId());
    }

    @Transactional
    public void updateBuilding(final UUID id, final BuildingRequestDto request) {
        buildingValidator.validate(request);

        final Building building = getBuildingOrThrow(id);
        buildingMapper.updateEntityFromDto(request, building);
        building.setUniversity(getUniversityOrThrow(request.universityId()));
        buildingService.save(building);
        log.info("updated building with ID: {}", building.getId());
    }

    @Transactional(readOnly = true)
    public BuildingResponseDto getBuildingById(final UUID id) {
        final Building building = getBuildingOrThrow(id);
        return buildingMapper.toResponseDto(building);
    }

    @Transactional
    public void deleteBuilding(final UUID id) {
        final Building building = getBuildingOrThrow(id);
        buildingService.delete(building);
        log.info("deleted building with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<BuildingResponseDto> getAllBuildings() {
        return buildingMapper.toResponseDtoList(buildingService.getAll());
    }

    private Building getBuildingOrThrow(final UUID id) {
        return buildingService.getById(id)
                              .orElseThrow(() -> new ResourceNotFoundException(
                                  BUILDING_NOT_FOUND.getMessage(String.valueOf(id)))
                              );
    }

    private University getUniversityOrThrow(final UUID universityId) {
        return universityService.getById(universityId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                    UNIVERSITY_NOT_FOUND.getMessage(String.valueOf(universityId))));
    }
}
