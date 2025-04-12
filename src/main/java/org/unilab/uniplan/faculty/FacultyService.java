package org.unilab.uniplan.faculty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;

    @Transactional
    public Optional<FacultyDto> createFaculty(final FacultyDto facultyDto) {
        final Faculty faculty = facultyMapper.toEntity(facultyDto);

        if (faculty.getFacultyName() == null || faculty.getUniversity() == null) {
            return Optional.empty();
        }

        final Faculty savedFaculty = facultyRepository.save(faculty);
        return Optional.of(facultyMapper.toDto(savedFaculty));
    }

    public List<FacultyDto> getAllFaculties() {
        final List<Faculty> faculties = facultyRepository.findAll();
        return facultyMapper.toDtoList(faculties);
    }

    public Optional<FacultyDto> getFacultyById(final UUID id) {
        final Optional<Faculty> faculty = facultyRepository.findById(id);
        return faculty.map(facultyMapper::toDto);
    }

    @Transactional
    public Optional<FacultyDto> updateFaculty(final UUID id, final FacultyDto facultyDto) {
        final Optional<Faculty> existingFaculty = facultyRepository.findById(id);

        if (existingFaculty.isPresent()) {
            final Faculty faculty = existingFaculty.get();
            facultyMapper.updateEntityFromDto(facultyDto, faculty);

            final Faculty savedFaculty = facultyRepository.save(faculty);
            return Optional.of(facultyMapper.toDto(savedFaculty));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<FacultyDto> deleteFaculty(final UUID id) {
        final Optional<Faculty> facultyOpt = facultyRepository.findById(id);

        if (facultyOpt.isPresent()) {
            final Faculty faculty = facultyOpt.get();
            facultyRepository.delete(faculty);
            return Optional.of(facultyMapper.toDto(faculty));
        }

        return Optional.empty();
    }
}
