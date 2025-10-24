package org.unilab.uniplan.programdiscipline;

import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineResponseDto;

@RestController
@RequestMapping("/program-disciplines")
@RequiredArgsConstructor
@Tag(name = "Program Disciplines", description = "Assign disciplines to academic programs, including lecture hours, exercise hours, and semester placement")
public class ProgramDisciplineController {

    private final ProgramDisciplineMapper programDisciplineMapper;
    private final ProgramDisciplineService programDisciplineService;

    @PostMapping("/add")
    public ResponseEntity<ProgramDisciplineResponseDto> createProgramDiscipline(@Valid @NotNull @RequestBody final
                                                                        ProgramDisciplineRequestDto programDisciplineRequestDto){
        final ProgramDisciplineDto programDisciplineDto = programDisciplineService
                                                            .createProgramDiscipline(programDisciplineMapper
                                                                                         .toInternalDto(programDisciplineRequestDto));
        return new ResponseEntity<>(programDisciplineMapper.toResponseDto(programDisciplineDto),
                                    HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public List<ProgramDisciplineResponseDto> getAllProgramDisciplines() {
        return programDisciplineMapper.toResponseDtoList(programDisciplineService.getAllProgramDisciplines());
    }

    @GetMapping("/getById")
    public ResponseEntity<ProgramDisciplineResponseDto> getProgramDisciplineById(@NotNull @RequestParam UUID disciplineId,
                                                                                 @NotNull @RequestParam UUID programId) {
        final ProgramDisciplineDto programDisciplineDto = programDisciplineService
            .getProgramDisciplineById(disciplineId, programId);

        return ok(programDisciplineMapper.toResponseDto(programDisciplineDto));
    }

    @PutMapping("/update")
    public ResponseEntity<ProgramDisciplineResponseDto> updateProgramDiscipline(@NotNull @RequestParam UUID disciplineId,
                                                                                @NotNull @RequestParam UUID programId,
                                                                                @NotNull @Valid @RequestBody ProgramDisciplineRequestDto programDisciplineRequestDto){
        final ProgramDisciplineDto programDisciplineDto = programDisciplineMapper
                                                            .toInternalDto(programDisciplineRequestDto);

        return ok(programDisciplineMapper.toResponseDto(programDisciplineService.updateProgramDiscipline(disciplineId,
                                                                programId,
                                                                programDisciplineDto)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProgramDiscipline(@NotNull @RequestParam UUID disciplineId,
                                                        @NotNull @RequestParam UUID programId) {
        programDisciplineService.deleteProgramDiscipline(disciplineId, programId);

        return ResponseEntity.noContent().build();
    }
}
