package org.unilab.uniplan.faculty;

import java.util.List;
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
@RequestMapping("/faculties")
public class FacultyController {

    private static final String FACULTY_NOT_FOUND = "The faculty is not found.";

    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping("/create")
    public FacultyDto createFaculty(@RequestBody FacultyDto facultyDto) {
        return facultyService.createFaculty(facultyDto);
    }

    @GetMapping
    public List<FacultyDto> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @GetMapping("/{id}")
    public FacultyDto getFacultyById(@PathVariable UUID id) {
        return facultyService.getFacultyById(id)
                             .orElseThrow(() -> new IllegalArgumentException(FACULTY_NOT_FOUND));
    }

    @PutMapping("/{id}")
    public FacultyDto updateFaculty(@PathVariable UUID id, @RequestBody FacultyDto facultyDto) {
        return facultyService.updateFaculty(id, facultyDto)
                             .orElseThrow(() -> new IllegalArgumentException(FACULTY_NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void deleteFaculty(@PathVariable UUID id) {
        boolean isDeleted = facultyService.deleteFaculty(id);
        if (!isDeleted) {
            throw new IllegalArgumentException(FACULTY_NOT_FOUND);
        }
    }
}
