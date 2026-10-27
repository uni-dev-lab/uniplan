package org.unilab.uniplan.university;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.university.dto.UniversityRequestDto;
import org.unilab.uniplan.university.dto.UniversityResponseDto;
import java.util.List;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.UNIVERSITY_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class UniversityWebFacade {

    private final UniversityMapper universityMapper;
    private final UniversityService universityService;

    @Transactional
    public void createUniversity(final UniversityRequestDto request) {
        final University university = universityMapper.toEntity(request);
        universityService.save(university);
        log.info("created university {} with ID: {}",
                 university.getUniName(),
                 university.getId());
    }

    @Transactional
    public void updateUniversity(final UUID id,
                                 final UniversityRequestDto request) {
        final University university = getUniversityOrThrow(id);

        universityMapper.updateEntity(request, university);
        universityService.save(university);
        log.info("updated university with ID: {}", university.getId());
    }

    @Transactional(readOnly = true)
    public List<UniversityResponseDto> getAllUniversities() {
        return universityMapper.toResponseDtoList(universityService.getAll());
    }

    @Transactional(readOnly = true)
    public UniversityResponseDto getUniversityById(final UUID id) {
        final University university = getUniversityOrThrow(id);
        return universityMapper.toResponseDto(university);
    }

    @Transactional
    public void deleteUniversity(final UUID id) {
        final University university = getUniversityOrThrow(id);
        universityService.delete(university);
        log.info("deleted university with ID: {}", id);
    }

    private University getUniversityOrThrow(final UUID id) {
        return universityService.getById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                    UNIVERSITY_NOT_FOUND.getMessage(String.valueOf(id))));
    }
}
