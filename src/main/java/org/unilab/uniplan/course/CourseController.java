package org.unilab.uniplan.course;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.text.MessageFormat;
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
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private static final String COURSE_NOT_FOUND = "Course with ID {0} not found.";

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @PostMapping
    public ResponseEntity<CourseResponseDTO> addCourse(@RequestBody @NotNull
                                                       @Valid final CourseRequestDTO courseRequestDTO) {
        final CourseDTO courseDTO = courseMapper.toInnerDTO(courseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(courseMapper.toResponseDTO(courseService.createCourse(courseDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getMajorById(@PathVariable @NotNull final UUID id) {
        return ResponseEntity.ok(courseMapper.toResponseDTO(courseService.findCourseById(id)
                                                                         .orElseThrow(() -> new ResponseStatusException(
                                                                             HttpStatus.NOT_FOUND,
                                                                             MessageFormat.format(
                                                                                 COURSE_NOT_FOUND,
                                                                                 id)))));
    }

    @GetMapping
    public List<CourseResponseDTO> getAllCourses() {
        return courseMapper.toResponseDTOList(courseService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable @NotNull final UUID id,
                                                          @RequestBody @NotNull @Valid final CourseRequestDTO courseRequestDTO) {
        final CourseDTO courseDTO = courseMapper.toInnerDTO(courseRequestDTO);
        return ResponseEntity.ok(courseMapper.toResponseDTO(courseService.updateCourse(id,
                                                                                       courseDTO)
                                                                         .orElseThrow(() -> new ResponseStatusException(
                                                                             HttpStatus.NOT_FOUND,
                                                                             MessageFormat.format(
                                                                                 COURSE_NOT_FOUND,
                                                                                 id)))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable @NotNull final UUID id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
