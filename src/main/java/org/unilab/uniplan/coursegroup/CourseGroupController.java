package org.unilab.uniplan.coursegroup;

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
@RequestMapping("/courseGroups")
public class CourseGroupController {

    private final CourseGroupService courseGroupService;

    @Autowired
    public CourseGroupController(final CourseGroupService courseGroupService) {
        this.courseGroupService = courseGroupService;
    }

    @PostMapping
    public ResponseEntity<CourseGroupDTO> addCourseGroup(@RequestBody final CourseGroupDTO courseGroupDTO) {
        return ResponseEntity.ok(courseGroupService.createCourseGroup(courseGroupDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CourseGroupDTO> getCourseGroup(@PathVariable final UUID id) {
        return ResponseEntity.ok(courseGroupService.findCourseGroupById(id)
                                                   .orElseThrow(()->new RuntimeException("CourseGroup not found")));
    }
    @GetMapping
    public ResponseEntity<List<CourseGroupDTO>> getAllCourseGroups() {
        return ResponseEntity.ok(courseGroupService.findAll());
    }
    @PostMapping("/{id}")
    public ResponseEntity<CourseGroupDTO> updateCourseGroup(@PathVariable final UUID id, @RequestBody final CourseGroupDTO courseGroupDTO) {
        return ResponseEntity.ok(courseGroupService.updateCourseGroup(id, courseGroupDTO));
    } 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseGroup(@PathVariable UUID id) {
        if(courseGroupService.deleteCourseGroup(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
