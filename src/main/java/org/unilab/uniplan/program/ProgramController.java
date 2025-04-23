package org.unilab.uniplan.program;

import jakarta.validation.Valid;
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
import org.unilab.uniplan.program.dto.ProgramDto;
import org.unilab.uniplan.program.dto.ProgramRequestDto;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/programs")
@RequiredArgsConstructor
public class ProgramController {

    public static final String PROGRAM_NOT_FOUND = "Program with ID {0} not found.";

    private final ProgramService programService;
    private final ProgramMapper programMapper;

    @PostMapping
    public ResponseEntity<ProgramDto> createProgram(@Valid @RequestBody ProgramRequestDto request) {
        final ProgramDto programDto = programService.createProgram(programMapper.toInternalDto(request));
        return new ResponseEntity<>(programDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProgramDto>> getAllPrograms() {
        return ResponseEntity.ok(programService.getAllPrograms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramDto> getProgramById(@PathVariable UUID id) {
        return programService.getProgramById(id)
                             .map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramDto> updateProgram(@PathVariable UUID id,
                                                    @Valid @RequestBody ProgramRequestDto request) {
        final ProgramDto programDto = programMapper.toInternalDto(request);

        return programService.updateProgram(id, programDto)
                              .map(ResponseEntity::ok)
                              .orElseThrow(() -> new ResponseStatusException(
                                  HttpStatus.NOT_FOUND,
                                  MessageFormat.format(PROGRAM_NOT_FOUND, id)
                              ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable UUID id) {
        programService.deleteProgram(id);

        return ResponseEntity.noContent().build();
    }
}

