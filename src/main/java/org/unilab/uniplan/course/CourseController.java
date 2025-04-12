package org.unilab.uniplan.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(final CourseService courseService) {
        this.courseService = courseService;
    }
    @PostMapping
    public ResponseEntity<CourseDTO> addCourse(@RequestBody final CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.createCourse(courseDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getMajorById(@PathVariable final UUID id) {
        return ResponseEntity.ok(courseService.findCourseById(id)
                                              .orElseThrow(()->new RuntimeException("Course with id " + id + " doesn't exists")));
    }
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }
    @PostMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable final UUID id, @RequestBody final CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable final UUID id) {
        if(courseService.deleteCourse(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
