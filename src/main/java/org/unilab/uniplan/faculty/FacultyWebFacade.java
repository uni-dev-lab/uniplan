package org.unilab.uniplan.faculty;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.dto.FacultyRequestDto;
import org.unilab.uniplan.faculty.dto.FacultyResponseDto;
import java.util.List;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.FACULTY_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class FacultyWebFacade {

    private final FacultyMapper facultyMapper;
    private final FacultyService facultyService;

    private Faculty getFacultyOrThrow(final UUID id) {
        return facultyService.getById(id)
                             .orElseThrow(() -> new ResourceNotFoundException(FACULTY_NOT_FOUND.getMessage(
                                 String.valueOf(id))));
    }

    @Transactional
    public void createFaculty(final FacultyRequestDto request) {
        final Faculty faculty = facultyMapper.toEntity(request);
        facultyService.save(faculty);
        log.info("created faculty {} with ID: {}",
                 faculty.getFacultyName(),
                 faculty.getId());
    }

    @Transactional(readOnly = true)
    public List<FacultyResponseDto> getAllFaculties() {
        return facultyMapper.toResponseDtoList(facultyService.getAll());
    }

    @Transactional
    public void deleteFaculty(final UUID id) {
        final Faculty faculty = getFacultyOrThrow(id);
        facultyService.delete(faculty);
        log.info("deleted faculty with ID: {}", id);

    }

    @Transactional(readOnly = true)
    public FacultyResponseDto getFacultyById(final UUID id) {
        final Faculty faculty = getFacultyOrThrow(id);
        return facultyMapper.toResponseDto(faculty);
    }


    @Transactional
    public void updateFaculty(final UUID id,
                              final FacultyRequestDto request) {
        final Faculty faculty = getFacultyOrThrow(id);
        facultyMapper.updateEntity(request, faculty);
        facultyService.save(faculty);
        log.info("updated faculty with ID: {}", faculty.getId());
    }
}