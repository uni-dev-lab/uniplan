package org.unilab.uniplan.building;

import static org.unilab.uniplan.utils.ErrorConstants.UNIVERSITY_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.building.dto.BuildingRequestDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.university.University;
import org.unilab.uniplan.university.UniversityService;

@Component
@RequiredArgsConstructor
public class BuildingValidator {

    private final UniversityService universityService;
    private final BuildingService buildingService;

    // Return the resolved university so the facade can reuse it without another lookup.
    public University validateAndResolveUniversity(
        final BuildingRequestDto requestDto) {

        final UUID universityId = requestDto.universityId();

        return universityService.getById(universityId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                    UNIVERSITY_NOT_FOUND.getMessage(
                                        String.valueOf(universityId)
                                    )
                                ));
    }

    public void validateCanDelete(final UUID buildingId) {
        if (buildingService.hasAssignedRooms(buildingId)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Cannot delete a building with assigned rooms"
            );
        }
    }
}