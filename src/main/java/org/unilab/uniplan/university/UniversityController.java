package org.unilab.uniplan.university;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
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

@RestController
@RequestMapping("/universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    @PostMapping
    public ResponseEntity<UniversityDto> createUniversity(@Valid @RequestBody final UniversityDto universityDto) {
        final Optional<UniversityDto> createdUniversity = universityService.createUniversity(
            universityDto);
        return createdUniversity
            .map(dto -> new ResponseEntity<>(dto, HttpStatus.CREATED))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity<List<UniversityDto>> getAllUniversities() {
        final List<UniversityDto> universities = universityService.getAllUniversities();
        return universities.isEmpty()
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
            : new ResponseEntity<>(universities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityDto> getUniversityById(@PathVariable final UUID id) {
        final Optional<UniversityDto> university = universityService.getUniversityById(id);
        return university
            .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UniversityDto> updateUniversity(@PathVariable final UUID id,
                                                          @Valid @RequestBody final UniversityDto universityDto) {
        final Optional<UniversityDto> updated = universityService.updateUniversity(id,
                                                                                   universityDto);
        return updated
            .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UniversityDto> deleteUniversity(@PathVariable final UUID id) {
        final Optional<UniversityDto> deleteUniversity = universityService.deleteUniversity(id);
        return deleteUniversity
            .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}