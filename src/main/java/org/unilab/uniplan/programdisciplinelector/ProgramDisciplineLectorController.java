package org.unilab.uniplan.programdisciplinelector;

import static org.springframework.http.ResponseEntity.ok;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorResponseDto;

@RestController
@RequestMapping("/programDisciplineLector")
@RequiredArgsConstructor
public class ProgramDisciplineLectorController {

    private final ProgramDisciplineLectorMapper programDisciplineLectorMapper;
    private final ProgramDisciplineLectorService programDisciplineLectorService;

    @PostMapping("/addProgramDisciplineLector")
    public ResponseEntity<ProgramDisciplineLectorResponseDto> createProgramDiscipline(@Valid @NotNull @RequestBody final
                                                                                      ProgramDisciplineLectorRequestDto programDisciplineLectorRequestDto){
        final ProgramDisciplineLectorDto programDisciplineLectorDto = programDisciplineLectorService
            .createProgramDisciplineLector(programDisciplineLectorMapper
                                               .toInternalDto(programDisciplineLectorRequestDto));

        return new ResponseEntity<>(programDisciplineLectorMapper.toResponseDto(programDisciplineLectorDto),
                                    HttpStatus.CREATED);
    }

    @GetMapping("/getAllProgramDisciplineLectors")
    public List<ProgramDisciplineLectorResponseDto> getAllProgramDisciplineLectors(){
        return programDisciplineLectorMapper.toResponseDtoList(programDisciplineLectorService.getAllProgramDisciplineLectors());
    }

    @GetMapping("/getProgramDisciplineLectorById")
    public ResponseEntity<ProgramDisciplineLectorResponseDto> getProgramDisciplineLectorById(@NotNull @RequestParam final UUID lectorId,
                                                                                             @NotNull @RequestParam final UUID programId,
                                                                                             @NotNull @RequestParam final UUID disciplineId) {
        final ProgramDisciplineLectorDto programDisciplineLectorDto = programDisciplineLectorService
            .getProgramDisciplineLectorById(lectorId, programId, disciplineId);

        return ok(programDisciplineLectorMapper.toResponseDto(programDisciplineLectorDto));
    }

    @PutMapping("/updateProgramDisciplineLector")
    public ResponseEntity<ProgramDisciplineLectorResponseDto> updateProgramDisciplineLector(@NotNull @RequestParam final UUID lectorId,
                                                                                            @NotNull @RequestParam final UUID programId,
                                                                                            @NotNull @RequestParam final UUID disciplineId,
                                                                                            @Valid @NotNull @RequestBody ProgramDisciplineLectorRequestDto programDisciplineLectorRequestDto){
        final ProgramDisciplineLectorDto programDisciplineLectorDto = programDisciplineLectorMapper.toInternalDto(programDisciplineLectorRequestDto);

        return ok(programDisciplineLectorMapper.toResponseDto(programDisciplineLectorService.updateProgramDisciplineLector(
            lectorId,
            programId,
            disciplineId,
            programDisciplineLectorDto)));
    }

    @DeleteMapping("/deleteProgramDisciplineLector")
    public ResponseEntity<Void> deleteProgramDisciplineLector(@NotNull @RequestParam final UUID lectorId,
                                                              @NotNull @RequestParam final UUID programId,
                                                              @NotNull @RequestParam final UUID disciplineId) {
        programDisciplineLectorService.deleteProgramDisciplineLector(lectorId,
                                                                     programId,
                                                                     disciplineId);

        return ResponseEntity.noContent().build();
    }
}
