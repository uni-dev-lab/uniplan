package org.unilab.uniplan.discipline;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.text.MessageFormat;
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
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.discipline.dto.DisciplineDto;
import org.unilab.uniplan.discipline.dto.DisciplineRequestDto;
import org.unilab.uniplan.discipline.dto.DisciplineResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/disciplines")
public class DisciplineController {

    private static final String DISCIPLINE_NOT_FOUND = "Discipline with ID {0} not found.";
    private final DisciplineMapper disciplineMapper;
    private final DisciplineService disciplineService;

    @PostMapping
    public ResponseEntity<DisciplineResponseDto> create(@Valid @NotNull @RequestBody final DisciplineRequestDto disciplineRequestDto) {
        final DisciplineDto disciplineDto = disciplineService.createDiscipline(
            disciplineMapper.toInternalDto(
                disciplineRequestDto));
        return new ResponseEntity<>(disciplineMapper.toResponseDto(disciplineDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<DisciplineResponseDto> getAllDisciplines() {
        return disciplineMapper.toResponseDtoList(disciplineService.getAllDisciplines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineResponseDto> getDisciplineById(@NotNull @PathVariable final UUID id) {
        final DisciplineDto disciplineDto = disciplineService.getDisciplineById(id)
                                                             .orElseThrow(() -> new ResponseStatusException(
                                                                 HttpStatus.NOT_FOUND,
                                                                 MessageFormat.format(
                                                                     DISCIPLINE_NOT_FOUND,
                                                                     id)));
        return ResponseEntity.ok(disciplineMapper.toResponseDto(disciplineDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisciplineResponseDto> update(@NotNull @PathVariable UUID id, @Valid @NotNull @RequestBody DisciplineRequestDto disciplineRequestDto) {
       final DisciplineDto internalDto =disciplineMapper.toInternalDto(disciplineRequestDto);

       return disciplineService.updateDiscipline(id, internalDto)
                               .map(disciplineMapper::toResponseDto)
                               .map(ResponseEntity::ok)
                               .orElseThrow(() -> new ResponseStatusException(
                                   HttpStatus.NOT_FOUND,
                                   MessageFormat.format(
                                       DISCIPLINE_NOT_FOUND,
                                       id)
                               ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiscipline(@NotNull @PathVariable UUID id) {
        disciplineService.deleteDiscipline(id);

        return ResponseEntity.noContent().build();
    }
}

