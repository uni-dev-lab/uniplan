package org.unilab.uniplan.faculty;

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
import org.unilab.uniplan.faculty.dto.FacultyRequestDto;
import org.unilab.uniplan.faculty.dto.FacultyResponseDto;

@RestController
@RequestMapping("/faculties")
@RequiredArgsConstructor
@Tag(name = "Faculties", description = "Manage faculties within universities, including name and location")
public class FacultyController {

    private final FacultyWebFacade facultyWebFacade;

    @PostMapping
    public ResponseEntity<FacultyResponseDto> createFaculty(
        @Valid @NotNull @RequestBody final FacultyRequestDto facultyRequestDto) {
        facultyWebFacade.createFaculty(
            facultyRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<FacultyResponseDto>> getAllFaculties() {
        return ResponseEntity.ok(facultyWebFacade.getAllFaculties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyResponseDto> getFacultyById(@NotNull @PathVariable final UUID id) {

        return ResponseEntity.ok(facultyWebFacade.getFacultyById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyResponseDto> updateFaculty(
        @PathVariable final UUID id,
        @Valid @NotNull @RequestBody final FacultyRequestDto facultyRequestDto) {
        facultyWebFacade.updateFaculty(id, facultyRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable final UUID id) {
        facultyWebFacade.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}
