package org.unilab.uniplan.faculty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;

@Service
@RequiredArgsConstructor
public class FacultyService implements BaseService<Faculty> {

    private final FacultyRepository facultyRepository;

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

    @Override
    public void delete(final Faculty faculty) {
        facultyRepository.delete(faculty);
    }
}