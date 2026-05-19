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
        log.info("Creating university {} at {}", request.uniName(), request.location());

        final University university = universityMapper.toEntity(request);
        final University savedUniversity = universityService.createUniversity(university);

        log.info("Created university {} with ID: {}",
                 savedUniversity.getUniName(),
                 savedUniversity.getId());
        return universityMapper.toResponseDto(savedUniversity);
    }

    @Transactional
    public UniversityResponseDto updateUniversity(final UUID id,
                                                  final UniversityRequestDto request) {
        log.info("Updating university with ID: {}", id);

        final University university = universityService.getUniversityById(id)
                                                       .orElseThrow(() -> new ResourceNotFoundException(
                                                           UNIVERSITY_NOT_FOUND.getMessage(String.valueOf(
                                                               id))));

        universityMapper.updateEntityFromRequestDto(request, university);
        final University savedUniversity = universityService.updateUniversity(university);
        log.info("Updated university with ID: {}", savedUniversity.getId());
        return universityMapper.toResponseDto(savedUniversity);
    }

    @Transactional(readOnly = true)
    public List<UniversityResponseDto> getAllUniversities() {
        return universityMapper.toResponseDtoList(universityService.getAllUniversities());
    }


    @Transactional(readOnly = true)
    public UniversityResponseDto getUniversityById(final UUID id) {
        final University university = universityService.getUniversityById(id)
                                                       .orElseThrow(() -> new ResourceNotFoundException
                                                           (UNIVERSITY_NOT_FOUND.getMessage(String.valueOf(
                                                               id))));
        return universityMapper.toResponseDto(university);
    }

    @Transactional
    public void deleteUniversity(final UUID id) {
        log.info("Deleting university with ID: {}", id);
        final University university = universityService.getUniversityById(id)
                                                       .orElseThrow(() -> new ResourceNotFoundException(
                                                           UNIVERSITY_NOT_FOUND.getMessage(String.valueOf(
                                                               id))));
        universityService.deleteUniversity(university);
        log.info("Deleted university with ID: {}", id);
    }

}
