package org.unilab.uniplan.lector;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectorService {
    private final LectorRepository lectorRepository;

    public Lector createLector(Lector lector) {
        if (lectorRepository.existsById(lector.getId())) {
            throw new IllegalArgumentException("Id already in use.");
        }
        return lectorRepository.save(lector);
    }

    public List<Lector> getAllLectors() {
        return lectorRepository.findAll();
    }

    public Lector getLectorById(UUID id) {
        return lectorRepository.findById(id)
                               .orElseThrow(() -> new EntityNotFoundException("Lector not found."));
    }

    public Lector updateLector(UUID id, Lector updatedLector) {
        Lector existingLector = getLectorById(id);
        if (!existingLector.getId().equals(updatedLector.getId()) &&
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

