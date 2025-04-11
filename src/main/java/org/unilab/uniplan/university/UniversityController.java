package org.unilab.uniplan.university;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UniversityController {

    private static final String UNIVERSITY_NOT_FOUND = "University not found.";

    private final UniversityService universityService;

    @Autowired
    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @PostMapping
    public UniversityDto createUniversity(@RequestBody UniversityDto universityDto) {
        return universityService.createUniversity(universityDto);
    }

    @GetMapping
    public List<UniversityDto> getAllUniversities() {
        return universityService.getAllUniversities();
    }

    @GetMapping("/{id}")
    public UniversityDto getUniversityById(@PathVariable UUID id) {
        Optional<UniversityDto> university = universityService.getUniversityById(id);
        return university.orElseThrow(() -> new IllegalArgumentException(UNIVERSITY_NOT_FOUND));
    }

    @PutMapping("/{id}")
    public UniversityDto updateUniversity(@PathVariable UUID id,
                                          @RequestBody UniversityDto universityDto) {
        return universityService.updateUniversity(id, universityDto)
                                .orElseThrow(() -> new IllegalArgumentException(UNIVERSITY_NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void deleteUniversity(@PathVariable UUID id) {
        boolean isDeleted = universityService.deleteUniversity(id);
        if (!isDeleted) {
            throw new IllegalArgumentException(UNIVERSITY_NOT_FOUND);
        }
    }
}
