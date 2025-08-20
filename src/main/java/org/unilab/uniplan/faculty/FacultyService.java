package org.unilab.uniplan.faculty;

import static org.unilab.uniplan.utils.ErrorConstants.FACULTY_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.dto.FacultyDto;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;

    @Transactional
    public FacultyDto createFaculty(final FacultyDto facultyDto) {
        final Faculty faculty = facultyMapper.toEntity(facultyDto);

        return saveEntityAndConvertToDto(faculty);
    }

    public List<FacultyDto> getAllFaculties() {
        final List<Faculty> faculties = facultyRepository.findAll();
        return facultyMapper.toDtoList(faculties);
    }

    public FacultyDto getFacultyById(final UUID id) {
        return facultyRepository.findById(id)
                                .map(facultyMapper::toDto)
                                .orElseThrow(() -> new ResourceNotFoundException(FACULTY_NOT_FOUND.getMessage(
                                    String.valueOf(id))));
    }

    @Transactional
    public FacultyDto updateFaculty(final UUID id, final FacultyDto facultyDto) {
        return facultyRepository.findById(id)
                                .map(existingFaculty -> updateEntityAndConvertToDto(
                                    facultyDto,
                                    existingFaculty))
                                .orElseThrow(() -> new ResourceNotFoundException(FACULTY_NOT_FOUND.getMessage(
                                    String.valueOf(id))));
    }

    @Transactional
    public void deleteFaculty(final UUID id) {
        final Faculty faculty = facultyRepository.findById(id)
                                                 .orElseThrow(() -> new ResourceNotFoundException(
                                                     FACULTY_NOT_FOUND.getMessage(String.valueOf(id))));
        facultyRepository.delete(faculty);
    }

    private FacultyDto updateEntityAndConvertToDto(final FacultyDto dto,
                                                   final Faculty entity) {
        facultyMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private FacultyDto saveEntityAndConvertToDto(final Faculty entity) {
        final Faculty savedEntity = facultyRepository.save(entity);
        return facultyMapper.toDto(savedEntity);
    }
}