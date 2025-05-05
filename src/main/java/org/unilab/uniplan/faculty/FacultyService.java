package org.unilab.uniplan.faculty;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.faculty.dto.FacultyDto;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private static final String FACULTY_NOT_FOUND = "Faculty with ID {0} not found.";

    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;

    @Transactional
    public FacultyDto createFaculty(final FacultyDto facultyDto) {
        final Faculty faculty = facultyMapper.toEntity(facultyDto);

        return facultyMapper.toDto(facultyRepository.save(faculty));
    }

    public List<FacultyDto> getAllFaculties() {
        final List<Faculty> faculties = facultyRepository.findAll();
        return facultyMapper.toDtoList(faculties);
    }

    public Optional<FacultyDto> getFacultyById(final UUID id) {
        return facultyRepository.findById(id)
                                .map(facultyMapper::toDto);
    }

    @Transactional
    public Optional<FacultyDto> updateFaculty(final UUID id, final FacultyDto facultyDto) {
        return facultyRepository.findById(id)
                                .map(existingFaculty -> {
                                    facultyMapper.updateEntityFromDto(facultyDto, existingFaculty);

                                    return facultyMapper.toDto(facultyRepository.save(
                                        existingFaculty));
                                });
    }

    @Transactional
    public void deleteFaculty(final UUID id) {
        final Faculty faculty = facultyRepository.findById(id)
                                                 .orElseThrow(() -> new RuntimeException(
                                                     MessageFormat.format(FACULTY_NOT_FOUND, id)));
        facultyRepository.delete(faculty);
    }
}