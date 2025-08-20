package org.unilab.uniplan.program;

import jakarta.validation.Valid;
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
import org.unilab.uniplan.program.dto.ProgramDto;
import org.unilab.uniplan.program.dto.ProgramRequestDto;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/programs")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;
    private final ProgramMapper programMapper;

    @PostMapping
    public ResponseEntity<ProgramDto> createProgram(@Valid @RequestBody ProgramRequestDto request) {
        final ProgramDto programDto = programService.createProgram(programMapper.toInternalDto(request));
        return new ResponseEntity<>(programDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProgramDto>> getAllPrograms() {
        return ok(programService.getAllPrograms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramDto> getProgramById(@PathVariable UUID id) {
        return ok(programService.getProgramById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramDto> updateProgram(@PathVariable UUID id,
                                                    @Valid @RequestBody ProgramRequestDto request) {
        final ProgramDto programDto = programMapper.toInternalDto(request);

        return ok(programService.updateProgram(id, programDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable UUID id) {
        programService.deleteProgram(id);

        return ResponseEntity.noContent().build();
    }
}

