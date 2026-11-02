package org.unilab.uniplan.programdiscipline;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineRequestDto;
import org.unilab.uniplan.programdiscipline.dto.ProgramDisciplineResponseDto;

@RestController
@RequestMapping("/program-disciplines")
@RequiredArgsConstructor
@Tag(name = "Program Disciplines",
    description = "Assign disciplines to academic programs,"
                  + " including lecture hours, exercise hours, and semester placement")
public class ProgramDisciplineController {

    private final ProgramDisciplineWebFacade programDisciplineWebFacade;

    @PostMapping("/add")
    public ResponseEntity<ProgramDisciplineResponseDto> createProgramDiscipline(
        @Valid @NotNull @RequestBody final ProgramDisciplineRequestDto request) {

        final ProgramDisciplineResponseDto response =
            programDisciplineWebFacade.createProgramDiscipline(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProgramDisciplineResponseDto>> getAllProgramDisciplines() {
        return ResponseEntity.ok(programDisciplineWebFacade.getAllProgramDisciplines());
    }

    @GetMapping("/getById")
    public ResponseEntity<ProgramDisciplineResponseDto> getProgramDisciplineById(
        @NotNull @RequestParam final UUID disciplineId,
        @NotNull @RequestParam final UUID programId) {

        return ResponseEntity.ok(programDisciplineWebFacade
                                     .getProgramDisciplineById(disciplineId, programId));
    }

    @PutMapping("/update")
    public ResponseEntity<ProgramDisciplineResponseDto> updateProgramDiscipline(
        @NotNull @RequestParam final UUID disciplineId,
        @NotNull @RequestParam final UUID programId,
        @NotNull @Valid @RequestBody final ProgramDisciplineRequestDto request) {

        return ResponseEntity.ok(programDisciplineWebFacade.updateProgramDiscipline(
            disciplineId, programId, request
        ));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProgramDiscipline(
        @NotNull @RequestParam final UUID disciplineId,
        @NotNull @RequestParam final UUID programId) {

        programDisciplineWebFacade.deleteProgramDiscipline(disciplineId, programId);

        return ResponseEntity.noContent().build();
    }
}
