package org.unilab.uniplan.university;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;

    public List<University> findAll() {
        return universityRepository.findAll();
    }

    public Optional<University> findById(final UUID id) {
        return universityRepository.findById(id);
    }

    public void delete(final University university) {
        universityRepository.delete(university);
    }

    public University save(final University university) {
        return universityRepository.save(university);
    }
}
