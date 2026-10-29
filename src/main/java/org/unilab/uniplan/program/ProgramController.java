package org.unilab.uniplan.program;

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
import org.unilab.uniplan.program.dto.ProgramRequestDto;
import org.unilab.uniplan.program.dto.ProgramResponseDto;

@RestController
@RequestMapping("/programs")
@RequiredArgsConstructor
@Tag(name = "Programs",
    description = "Manage academic programs (e.g., Software Engineering"
                  + " – Bachelor) linked to a course and composed of multiple disciplines")
public class ProgramController {

    private final ProgramWebFacade programWebFacade;

    @PostMapping
    public ResponseEntity<ProgramResponseDto> createProgram(
        @Valid @RequestBody @NotNull final ProgramRequestDto request) {
        final ProgramResponseDto response = programWebFacade.createProgram(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProgramResponseDto>> getAllPrograms() {
        return ResponseEntity.ok(programWebFacade.getAllPrograms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramResponseDto> getProgramById(
        @PathVariable @NotNull final UUID id) {
        return ResponseEntity.ok(programWebFacade.getProgramById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramResponseDto> updateProgram(
        @PathVariable @NotNull final UUID id,
        @Valid @RequestBody @NotNull final ProgramRequestDto request) {
        return ResponseEntity.ok(programWebFacade.updateProgram(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(
        @PathVariable @NotNull final UUID id) {
        programWebFacade.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}

