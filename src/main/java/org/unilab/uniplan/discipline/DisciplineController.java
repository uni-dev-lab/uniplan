package org.unilab.uniplan.discipline;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/disciplines")
public class DisciplineController {

    private final DisciplineService disciplineService;

    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @PostMapping
    public ResponseEntity<DisciplineDto> create(@Valid @RequestBody DisciplineDto dto) {
        DisciplineDto created = disciplineService.createDiscipline(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DisciplineDto>> getAll() {
        return ResponseEntity.ok(disciplineService.getAllDisciplines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(disciplineService.getDisciplineById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplineDto> update(@PathVariable UUID id, @Valid @RequestBody DisciplineDto dto) {
        DisciplineDto updated = disciplineService.updateDiscipline(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        disciplineService.deleteDiscipline(id);
        return ResponseEntity.noContent().build();
    }
}

