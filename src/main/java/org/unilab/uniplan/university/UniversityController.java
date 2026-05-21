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
    public ResponseEntity<UniversityResponseDto> createUniversity(
        @Valid @NotNull @RequestBody final UniversityRequestDto universityRequestDto) {
        return new ResponseEntity<>(universityWebFacade.createUniversity(universityRequestDto),
                                    HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UniversityResponseDto>> getAllUniversities() {
        return ResponseEntity.ok(universityWebFacade.getAllUniversities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityResponseDto> getUniversityById(@NotNull @PathVariable final UUID id) {
        return ResponseEntity.ok(universityWebFacade.getUniversityById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UniversityResponseDto> updateUniversity(@NotNull
                                                                  @PathVariable final UUID id,
                                                                  @Valid @NotNull @RequestBody final UniversityRequestDto universityRequestDto) {
        return ResponseEntity.ok(universityWebFacade.updateUniversity(id, universityRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@NotNull @PathVariable final UUID id) {
        universityWebFacade.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }
}
