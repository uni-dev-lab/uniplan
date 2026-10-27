package org.unilab.uniplan.major;

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
import org.unilab.uniplan.major.dto.MajorCoursesResponseDto;
import org.unilab.uniplan.major.dto.MajorRequestDto;
import org.unilab.uniplan.major.dto.MajorResponseDto;

@RestController
@RequestMapping("/majors")
@RequiredArgsConstructor
@Tag(name = "Majors", description = "Manage academic majors (e.g., Informatics, Software Engineering) associated with faculties")
public class MajorController {

    private final MajorWebFacade majorWebFacade;

    @PostMapping
    public ResponseEntity<Void> createMajor(@RequestBody @NotNull
                                                     @Valid final MajorRequestDto majorRequestDTO) {
        majorWebFacade.createMajor(majorRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MajorResponseDto> getMajorById(@PathVariable final UUID id) {
        return ResponseEntity.ok(majorWebFacade.getMajorById(id));
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<MajorCoursesResponseDto> getMajorWithCoursesById(@PathVariable final UUID id) {
        return ResponseEntity.ok(majorWebFacade.getMajorWithCoursesById(id));
    }

    @GetMapping("/faculty/{facultyId}")
    public  ResponseEntity<List<MajorResponseDto>> getMajorsByFacultyId(@PathVariable final UUID facultyId) {
        return ResponseEntity.ok(majorWebFacade.getMajorsByFacultyId(facultyId));
    }

    @GetMapping("/faculty/{facultyId}/courses")
    public  ResponseEntity<List<MajorCoursesResponseDto>> getMajorsWithCoursesByFacultyId(@PathVariable final UUID facultyId) {
        return ResponseEntity.ok(majorWebFacade.getMajorsWithCoursesByFacultyId(facultyId));
    }

    @GetMapping
    public ResponseEntity<List<MajorResponseDto>> getAllMajors() {
        return ResponseEntity.ok(majorWebFacade.getAllMajors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMajor(@PathVariable final UUID id,
                                                        @RequestBody @NotNull @Valid MajorRequestDto majorRequestDTO) {
        majorWebFacade.updateMajor(id, majorRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable final UUID id) {
        majorWebFacade.deleteMajor(id);
        return ResponseEntity.noContent().build();
    }
}