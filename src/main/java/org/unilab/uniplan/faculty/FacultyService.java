package org.unilab.uniplan.faculty;

<<<<<<< feature/104-add-soft-delete-on-faculty
import static org.unilab.uniplan.utils.ErrorConstants.FACULTY_NOT_FOUND;

import java.time.LocalDateTime;
=======
>>>>>>> main
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
<<<<<<< feature/104-add-soft-delete-on-faculty
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.dto.FacultyDto;
import org.unilab.uniplan.major.Major;
import org.unilab.uniplan.major.MajorRepository;
=======
import org.unilab.uniplan.common.model.BaseService;
>>>>>>> main

@Service
@RequiredArgsConstructor
public class FacultyService implements BaseService<Faculty> {

    private final FacultyRepository facultyRepository;
<<<<<<< feature/104-add-soft-delete-on-faculty
    private final FacultyMapper facultyMapper;
    private final MajorRepository majorRepository;

    @Transactional
    public FacultyDto createFaculty(final FacultyDto facultyDto) {
        final Faculty faculty = facultyMapper.toEntity(facultyDto);

        return saveEntityAndConvertToDto(faculty);
    }
=======
>>>>>>> main

    @Override
    public void save(final Faculty faculty) {
        facultyRepository.save(faculty);
    }

    @Override
    public List<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    @Override
    public Optional<Faculty> getById(final UUID id) {
        return facultyRepository.findById(id);
    }

<<<<<<< feature/104-add-soft-delete-on-faculty
    @Transactional
    public void deleteFaculty(final UUID id) {
        final Faculty faculty = facultyRepository.findById(id)
                                                 .orElseThrow(() -> new ResourceNotFoundException(
                                                     FACULTY_NOT_FOUND.getMessage(String.valueOf(id))));

        majorRepository.deleteAll(majorRepository.findAllByFaculty(faculty));

=======
    @Override
    public void delete(final Faculty faculty) {
>>>>>>> main
        facultyRepository.delete(faculty);
    }
}