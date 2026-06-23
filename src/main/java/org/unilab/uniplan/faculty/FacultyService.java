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

    @Transactional
    public Faculty save(final Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Transactional(readOnly = true)
    public List<Faculty> getAll() {
       return facultyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Faculty> getById(final UUID id) {
      return facultyRepository.findById(id);
    }

    @Transactional
    public void delete(final Faculty faculty) {
        facultyRepository.delete(faculty);
    }
}