package org.unilab.uniplan.programdiscipline;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineResponseDto;

@RestController
@RequestMapping("/programDisciplines")
@RequiredArgsConstructor
public class ProgramDisciplineController {

    private final ProgramDisciplineMapper programDisciplineMapper;
    private final ProgramDisciplineService programDisciplineService;

    @PostMapping
    public ResponseEntity<ProgramDisciplineResponseDto> createProgramDiscipline(@Valid @NotNull @RequestBody final
                                                                        ProgramDisciplineRequestDto programDisciplineRequestDto){
        final ProgramDisciplineDto programDisciplineDto = programDisciplineService
                                                            .createProgramDiscipline(programDisciplineMapper
                                                                                         .toInternalDto(programDisciplineRequestDto));
        return new ResponseEntity<>(programDisciplineMapper.toResponseDto(programDisciplineDto),
                                    HttpStatus.CREATED);
    }

    @GetMapping
    public List<ProgramDisciplineResponseDto> getAllProgramDisciplines() {
        return programDisciplineMapper.toResponseDtoList(programDisciplineService.getAllProgramDisciplines());
    }

    @GetMapping("/{disciplineId}/{programId}")
    public ResponseEntity<ProgramDisciplineResponseDto> getProgramDisciplineById(@NotNull @PathVariable UUID disciplineId,
                                                                                 @NotNull @PathVariable UUID programId) {
        final ProgramDisciplineDto programDisciplineDto = programDisciplineService
            .getProgramDisciplineById(disciplineId, programId);

        return ok(programDisciplineMapper.toResponseDto(programDisciplineDto));
    }

    @PutMapping("/{disciplineId}/{programId}")
    public ResponseEntity<ProgramDisciplineResponseDto> updateProgramDiscipline(@NotNull @PathVariable UUID disciplineId,
                                                                                @NotNull @PathVariable UUID programId,
                                                                                @NotNull @Valid @RequestBody ProgramDisciplineRequestDto programDisciplineRequestDto){
        final ProgramDisciplineDto programDisciplineDto = programDisciplineMapper
                                                            .toInternalDto(programDisciplineRequestDto);

        return ok(programDisciplineMapper.toResponseDto(programDisciplineService.updateProgramDiscipline(disciplineId,
                                                                programId,
                                                                programDisciplineDto)));
    }

    @DeleteMapping("/{disciplineId}/{programId}")
    public ResponseEntity<Void> deleteProgramDiscipline(@NotNull @PathVariable UUID disciplineId,
                                                        @NotNull @PathVariable UUID programId) {
        programDisciplineService.deleteProgramDiscipline(disciplineId, programId);

        return ResponseEntity.noContent().build();
    }
}
