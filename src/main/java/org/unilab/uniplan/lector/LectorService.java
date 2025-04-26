package org.unilab.uniplan.lector;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public LectorDto createLector(LectorDto lectorDto) {
        Lector lector = lectorMapper.toEntity(lectorDto);
        lector = lectorRepository.save(lector);
        return lectorMapper.toDto(lector);
    }

    public List<LectorDto> getAllLectors() {
        List<Lector> lectors = lectorRepository.findAll();
        return lectorMapper.toDtos(lectors);
    }

    public Optional<LectorDto> getLectorById(UUID id) {
        Optional<Lector> lector = lectorRepository.findById(id);
        return lector.map(lectorMapper::toDto);
    }

    public LectorDto updateLector(UUID id, LectorDto updatedLector) {
        Lector existingLector = lectorRepository.findById(id)
                                                .orElseThrow(() -> new EntityNotFoundException("Lector not found with id: " + id));

        if (!id.equals(updatedLector.id()) &&
            lectorRepository.existsById(updatedLector.id())) {
            throw new IllegalArgumentException("Id already in use.");
        }

        lectorMapper.updateEntity(updatedLector, existingLector);

        Lector savedLector = lectorRepository.save(existingLector);
        return lectorMapper.toDto(savedLector);
    }

    public void deleteLector(UUID id) {
        if (!lectorRepository.existsById(id)) {
            throw new EntityNotFoundException("Lector not found.");
        }
        lectorRepository.deleteById(id);
    }
}

