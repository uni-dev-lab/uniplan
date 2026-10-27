package org.unilab.uniplan.university;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;

@Service
@RequiredArgsConstructor
public class UniversityService implements BaseService<University> {

    private final UniversityRepository universityRepository;

    @Override
    public List<University> getAll() {
        return universityRepository.findAll();
    }

    @Override
    public Optional<University> getById(final UUID id) {
        return universityRepository.findById(id);
    }

    @Override
    public void delete(final University university) {
        universityRepository.delete(university);
    }

    @Override
    public void save(final University university) {
        universityRepository.save(university);
    }
}
