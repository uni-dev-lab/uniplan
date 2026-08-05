package org.unilab.uniplan.building;

import static org.unilab.uniplan.utils.ErrorConstants.UNIVERSITY_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.building.dto.BuildingRequestDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.university.UniversityRepository;

@Component
@RequiredArgsConstructor
public class BuildingValidator {

    private final UniversityRepository universityRepository;

    public void validate(final BuildingRequestDto requestDto) {
        validateUniversityExists(requestDto.universityId());
    }

    private void validateUniversityExists(final UUID universityId) {
        if (!universityRepository.existsById(universityId)) {
            throw new ResourceNotFoundException(
                UNIVERSITY_NOT_FOUND.getMessage(String.valueOf(universityId))
            );
        }
    }
}