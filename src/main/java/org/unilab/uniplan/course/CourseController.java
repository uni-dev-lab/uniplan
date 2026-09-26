package org.unilab.uniplan.course;

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
import org.unilab.uniplan.course.dto.CourseDto;
import org.unilab.uniplan.course.dto.CourseRequestDto;
import org.unilab.uniplan.course.dto.CourseResponseDto;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Tag(
    name = "Courses",
    description = "Manage university courses, including year of study, degree type (Bachelor, Master, PhD), and study form (Regular, Part-time, Distance)"
)
public class CourseController {

    private final CourseWebFacade courseWebFacade;

    @PostMapping
    public ResponseEntity<Void> addCourse(@RequestBody @NotNull
                                                       @Valid final CourseRequestDto courseRequestDTO) {
        courseWebFacade.createCourse(courseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/major/{majorId}")
    public  ResponseEntity<List<CourseResponseDto>> getCoursesByMajorId(@PathVariable final UUID majorId) {
        return ResponseEntity.ok(courseWebFacade.getCoursesByMajorId(majorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(@PathVariable final UUID id) {
        return ResponseEntity.ok(courseWebFacade.getCourseById(id));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        return ResponseEntity.ok(courseWebFacade.getAllCourses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable final UUID id,
                                                          @RequestBody @NotNull @Valid final CourseRequestDto courseRequestDTO) {
        courseWebFacade.updateCourse(id, courseRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable final UUID id) {
        courseWebFacade.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}