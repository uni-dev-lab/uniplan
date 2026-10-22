package org.unilab.uniplan.programdisciplinelector;


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
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorRequestDto;
import org.unilab.uniplan.programdisciplinelector.dto.ProgramDisciplineLectorResponseDto;

@RestController
@RequestMapping("/program-discipline-lectors")
@RequiredArgsConstructor
@Tag(name = "Program Discipline Lectors",
    description = "Assign lecturers to program disciplines with specific roles (e.g., Main Lecturer, Assistant)")
public class ProgramDisciplineLectorController {

    private final ProgramDisciplineLectorWebFacade programDisciplineLectorWebFacade;

    @PostMapping("/add")
    public ResponseEntity<ProgramDisciplineLectorResponseDto> createProgramDisciplineLector(
        @Valid @NotNull @RequestBody final ProgramDisciplineLectorRequestDto request) {

        final ProgramDisciplineLectorResponseDto response =
            programDisciplineLectorWebFacade.createProgramDisciplineLector(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProgramDisciplineLectorResponseDto>> getAllProgramDisciplineLectors() {
        return ResponseEntity.ok(programDisciplineLectorWebFacade.getAllProgramDisciplineLectors());
    }

    @GetMapping("/getById")
    public ResponseEntity<ProgramDisciplineLectorResponseDto> getProgramDisciplineLectorById(
        @NotNull @RequestParam final UUID lectorId,
        @NotNull @RequestParam final UUID programId,
        @NotNull @RequestParam final UUID disciplineId) {

        return ResponseEntity.ok(programDisciplineLectorWebFacade.getProgramDisciplineLectorById(
            lectorId,
            programId,
            disciplineId));
    }

    @PutMapping("/update")
    public ResponseEntity<ProgramDisciplineLectorResponseDto> updateProgramDisciplineLector(
        @NotNull @RequestParam final UUID lectorId,
        @NotNull @RequestParam final UUID programId,
        @NotNull @RequestParam final UUID disciplineId,
        @Valid @NotNull @RequestBody final ProgramDisciplineLectorRequestDto request) {

        return ResponseEntity.ok(programDisciplineLectorWebFacade.updateProgramDisciplineLector(
            lectorId,
            programId,
            disciplineId,
            request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProgramDisciplineLector(
        @NotNull @RequestParam final UUID lectorId,
        @NotNull @RequestParam final UUID programId,
        @NotNull @RequestParam final UUID disciplineId) {

        programDisciplineLectorWebFacade.deleteProgramDisciplineLector(lectorId,
                                                                       programId,
                                                                       disciplineId);

        return ResponseEntity.noContent().build();
    }
}
