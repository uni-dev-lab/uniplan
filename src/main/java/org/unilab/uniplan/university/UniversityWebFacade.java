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
    public UniversityResponseDto createUniversity(final UniversityRequestDto request) {
        final University university = universityMapper.createEntity(request);
        final University savedUniversity = universityService.save(university);
        log.info("created university {} with ID: {}",
                 savedUniversity.getUniName(),
                 savedUniversity.getId());
        return universityMapper.toResponseDto(savedUniversity);
    }

    @Transactional
    public UniversityResponseDto updateUniversity(final UUID id,
                                                  final UniversityRequestDto request) {
        final University university = findUniversity(id);

        universityMapper.updateEntity(request, university);
        final University savedUniversity = universityService.save(university);
        log.info("updated university with ID: {}", savedUniversity.getId());
        return universityMapper.toResponseDto(savedUniversity);
    }

    @Transactional(readOnly = true)
    public List<UniversityResponseDto> getAllUniversities() {
        return universityMapper.toResponseDtoList(universityService.findAll());
    }

    @Transactional(readOnly = true)
    public UniversityResponseDto getUniversityById(final UUID id) {
        final University university = findUniversity(id);
        return universityMapper.toResponseDto(university);
    }

    @Transactional
    public void deleteUniversity(final UUID id) {
        final University university = findUniversity(id);
        universityService.delete(university);
        log.info("deleted university with ID: {}", id);
    }

    private University findUniversity(final UUID id) {
        return universityService.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                    UNIVERSITY_NOT_FOUND.getMessage(String.valueOf(id))));
    }
}
