package org.unilab.uniplan.discipline;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
import org.unilab.uniplan.discipline.dto.DisciplineRequestDto;
import org.unilab.uniplan.discipline.dto.DisciplineResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/disciplines")
@Tag(name = "Disciplines", description = "Manage academic disciplines (e.g., Algorithms, Databases)"
                                         + " with assigned main lecturers, linked to academic programs")
public class DisciplineController {

    private final DisciplineWebFacade disciplineWebFacade;

    @PostMapping
    public ResponseEntity<DisciplineResponseDto> create(
        @Valid @NotNull @RequestBody final DisciplineRequestDto request) {

        final DisciplineResponseDto response = disciplineWebFacade.createDiscipline(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<DisciplineResponseDto> getAllDisciplines() {
        return disciplineWebFacade.getAllDisciplines();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineResponseDto> getDisciplineById(@PathVariable final UUID id) {
        return ResponseEntity.ok(disciplineWebFacade.getDisciplineById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplineResponseDto> update(
        @PathVariable final UUID id,
        @Valid @NotNull @RequestBody final DisciplineRequestDto request) {
        return ResponseEntity.ok(disciplineWebFacade.updateDiscipline(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscipline(@PathVariable final UUID id) {
        disciplineWebFacade.deleteDiscipline(id);
        return ResponseEntity.noContent().build();
    }
}

