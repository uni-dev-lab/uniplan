package org.unilab.uniplan.lector;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/lectors")
@RequiredArgsConstructor
public class LectorController {
    private final LectorService lectorService;

    @PostMapping
    public ResponseEntity<LectorDto> createLector(@Valid @RequestBody LectorDto lectorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lectorService.createLector(lectorDto));
    }

    @GetMapping
    public ResponseEntity<List<LectorDto>> getAllLectors() {
        return ResponseEntity.ok(lectorService.getAllLectors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectorDto> getLectorById(@PathVariable UUID id) {
        final LectorDto lectorDto = lectorService.getLectorById(id)
                                                       .orElseThrow(() -> new ResponseStatusException(
                                                           HttpStatus.NOT_FOUND,
                                                           MessageFormat.format("LECTOR_NOT_FOUND", id)
                                                       ));
        return ResponseEntity.ok(lectorDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LectorDto> updateLector(@PathVariable UUID id, @Valid @RequestBody LectorDto lector) {
        return ResponseEntity.ok(lectorService.updateLector(id, lector));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLector(@PathVariable UUID id) {
        try {
            lectorService.deleteLector(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
