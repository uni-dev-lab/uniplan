package org.unilab.uniplan.programdisciplinelector;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorResponseDto;

@RestController
@RequestMapping("/api/ProgramDisciplineLector")
@RequiredArgsConstructor
public class ProgramDisciplineLectorController {

    private static final String PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND = "Program discipline lector with lectorId {0}, programId {1} and disciplineId {2} not found.";
    private final ProgramDisciplineLectorMapper programDisciplineLectorMapper;
    private final ProgramDisciplineLectorService programDisciplineLectorService;

    @PostMapping
    public ResponseEntity<ProgramDisciplineLectorResponseDto> createProgramDiscipline(@Valid @NotNull @RequestBody final
                                                                                      ProgramDisciplineLectorRequestDto programDisciplineLectorRequestDto){
        final ProgramDisciplineLectorDto programDisciplineLectorDto = programDisciplineLectorService
            .createProgramDisciplineLector(programDisciplineLectorMapper
                                               .toInternalDto(programDisciplineLectorRequestDto));

        return new ResponseEntity<>(programDisciplineLectorMapper.toResponseDto(programDisciplineLectorDto),
                                    HttpStatus.CREATED);
    }

    @GetMapping
    public List<ProgramDisciplineLectorResponseDto> getAllProgramDisciplineLectors(){
        return programDisciplineLectorMapper.toResponseDtoList(programDisciplineLectorService.getAllProgramDisciplineLectors());
    }

    @GetMapping("/{lectorId}/{programId}/{disciplineId}")
    public ResponseEntity<ProgramDisciplineLectorResponseDto> getProgramDisciplineLectorById(@NotNull @PathVariable final UUID lectorId,
                                                                                             @NotNull @PathVariable final UUID programId,
                                                                                             @NotNull @PathVariable final UUID disciplineId) {
        final ProgramDisciplineLectorDto programDisciplineLectorDto = programDisciplineLectorService
            .getProgramDisciplineLectorById(lectorId, programId, disciplineId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                           MessageFormat.format(PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND, id)
            ));
        return ResponseEntity.ok(programDisciplineLectorMapper.toResponseDto(programDisciplineLectorDto));
    }

    @PutMapping("/{lectorId}/{programId}/{disciplineId}")
    public ResponseEntity<ProgramDisciplineLectorResponseDto> updateProgramDisciplineLector(@NotNull @PathVariable final UUID lectorId,
                                                                                            @NotNull @PathVariable final UUID programId,
                                                                                            @NotNull @PathVariable final UUID disciplineId,
                                                                                            @Valid @NotNull @RequestBody ProgramDisciplineLectorRequestDto programDisciplineLectorRequestDto){
        final ProgramDisciplineLectorDto programDisciplineLectorDto = programDisciplineLectorMapper.toInternalDto(programDisciplineLectorRequestDto);

        return programDisciplineLectorService.updateProgramDisciplineLector(lectorId,
                                                                            programId,
                                                                            disciplineId,
                                                                            programDisciplineLectorDto)
                                             .map(programDisciplineLectorMapper::toResponseDto)
                                             .map(ResponseEntity::ok)
                                             .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                MessageFormat.format(PROGRAM_DISCIPLINE_LECTOR_NOT_FOUND,id)
                                             ));
    }

    @DeleteMapping("/{lectorId}/{programId}/{disciplineId}")
    public ResponseEntity<Void> deleteProgramDisciplineLector(@NotNull @PathVariable final UUID lectorId,
                                                              @NotNull @PathVariable final UUID programId,
                                                              @NotNull @PathVariable final UUID disciplineId) {
        programDisciplineLectorService.deleteProgramDisciplineLector(lectorId,
                                                                     programId,
                                                                     disciplineId);

        return ResponseEntity.noContent().build();
    }
}
