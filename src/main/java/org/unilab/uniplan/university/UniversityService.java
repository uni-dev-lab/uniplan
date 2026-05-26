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

    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    public Optional<University> getUniversityById(final UUID id) {
        return universityRepository.findById(id);
    }

    public void deleteUniversity(final University university) {
        universityRepository.delete(university);
    }

    public University saveUniversity(final University university) {
        return universityRepository.save(university);
    }
}
