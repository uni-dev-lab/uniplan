package org.unilab.uniplan.lector;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Transient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectorService {
    private final LectorRepository lectorRepository;

    private final LectorMapper lectorMapper;

    private final FacultyService facultyService;

    @Transient
    public LectorDto createLector(LectorDto lectorDto) {
        if (lectorRepository.existsById(lectorDto.id())) {
            throw new IllegalArgumentException("Id already in use.");
        }
        Faculty faculty = facultyService.getFaculty(lectorDto.facultyId())
                                        .orElseThrow(() -> new IllegalArgumentException("University not found"));

        Lector lector = lectorMapper.toEntity(lectorDto);
        lector.setFaculty(faculty);
        lector.setEmail(lectorDto.email());
        lector = lectorRepository.save(lector);
        return lectorMapper.toDto(lector);
    }

    public List<LectorDto> getAllLectors() {
        List<Lector> lectors = lectorRepository.findAll();
        return lectors.stream().map(lectorMapper::toDto).toList();
    }

    public Optional<LectorDto> getLectorById(UUID id) {
        Optional<Lector> lector = lectorRepository.findById(id);
        return lector.map(lectorMapper::toDto);

    }

    @Transient
    public Lector updateLector(UUID id, Lector updatedLector) {
        Lector existingLector = lectorRepository.findById(id)
                                                .orElseThrow(() -> new EntityNotFoundException("Lector not found with id: " + id));

        if (!id.equals(updatedLector.getId()) &&
            lectorRepository.existsById(updatedLector.getId())) {
            throw new IllegalArgumentException("Id already in use.");
        }

        existingLector.setFaculty(updatedLector.getFaculty());
        existingLector.setEmail(updatedLector.getEmail());

        return lectorRepository.save(existingLector);
    }

    public void deleteLector(UUID id) {
        if (!lectorRepository.existsById(id)) {
            throw new EntityNotFoundException("Lector not found.");
        }
        lectorRepository.deleteById(id);
    }
}

