package org.unilab.uniplan.programdiscipline;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineResponseDto;
import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping("/api/programDisciplines")
@RequiredArgsConstructor
public class ProgramDisciplineController {

    private static final String PROGRAM_DISCIPLINE_NOT_FOUND = "Lector with ID {0} not found.";
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

    @GetMapping("/{id}")
    public ResponseEntity<ProgramDisciplineResponseDto> getProgramDisciplineById(@NotNull @PathVariable ProgramDisciplineId id){
        final ProgramDisciplineDto programDisciplineDto = programDisciplineService
                                                            .getProgramDisciplineById(id)
                                                            .orElseThrow(()-> new ResponseStatusException(
                                                                HttpStatus.NOT_FOUND,
                                                                MessageFormat.format(PROGRAM_DISCIPLINE_NOT_FOUND, id)
                                                            ));
        return ResponseEntity.ok(programDisciplineMapper.toResponseDto(programDisciplineDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramDisciplineResponseDto> updateProgramDiscipline(@NotNull @PathVariable ProgramDisciplineId id,
                                                                                @NotNull @Valid @RequestBody ProgramDisciplineRequestDto programDisciplineRequestDto){
        final ProgramDisciplineDto programDisciplineDto = programDisciplineMapper
                                                            .toInternalDto(programDisciplineRequestDto);

        return programDisciplineService.updateProgramDiscipline(id , programDisciplineDto)
                                       .map(programDisciplineMapper::toResponseDto)
                                       .map(ResponseEntity::ok)
                                       .orElseThrow(() -> new ResponseStatusException(
                                           HttpStatus.NOT_FOUND,
                                           MessageFormat.format(PROGRAM_DISCIPLINE_NOT_FOUND,id)
                                       ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgramDiscipline(@NotNull @PathVariable ProgramDisciplineId id){
        programDisciplineService.deleteProgramDiscipline(id);

        return ResponseEntity.noContent().build();
    }
}
