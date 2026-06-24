package org.unilab.uniplan.university;

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
import org.unilab.uniplan.university.dto.UniversityRequestDto;
import org.unilab.uniplan.university.dto.UniversityResponseDto;

@RestController
@RequestMapping("/universities")
@RequiredArgsConstructor
@Tag(name = "Universities", description = "Manage universities, including name, location, accreditation, establishment year, and website")
public class UniversityController {

    private final UniversityWebFacade universityWebFacade;

    @PostMapping
    public ResponseEntity<Void> createUniversity(@RequestBody @Valid @NotNull final UniversityRequestDto universityRequestDto) {
        universityWebFacade.createUniversity(
            universityRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<UniversityResponseDto>> getAllUniversities() {
        return ResponseEntity.ok(universityWebFacade.getAllUniversities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityResponseDto> getUniversityById(@PathVariable final UUID id) {
        return ResponseEntity.ok(universityWebFacade.getUniversityById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUniversity(@PathVariable final UUID id,
                                                                  @RequestBody @Valid @NotNull final UniversityRequestDto universityRequestDto) {
        universityWebFacade.updateUniversity(id, universityRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable final UUID id) {
        universityWebFacade.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }
}