package org.unilab.uniplan.studentgroup;

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
@RequestMapping("/studentGroups")
public class StudentGroupController {
    
    private final StudentGroupService studentGroupService;

    @Autowired
    public StudentGroupController(final StudentGroupService studentGroupService) {
        this.studentGroupService = studentGroupService;
    }

    @PostMapping
    public ResponseEntity<StudentGroupDTO> addStudentGroup(@RequestBody final StudentGroupDTO studentGroupDTO) {
        return ResponseEntity.ok(studentGroupService.createStudentGroup(studentGroupDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<StudentGroupDTO> getStudentGroupById(@PathVariable final UUID id) {
        return ResponseEntity.ok(studentGroupService.findStudentGroupById(id)
                                                    .orElseThrow(()->new RuntimeException("Student group not found")));
    }
    @GetMapping
    public ResponseEntity<List<StudentGroupDTO>> getAllStudentGroups() {
        return ResponseEntity.ok(studentGroupService.findAll());
    }
    @PostMapping("/{id}")
    public ResponseEntity<StudentGroupDTO> updateStudentGroup(@PathVariable final UUID id, @RequestBody final StudentGroupDTO studentGroupDTO) {
        return ResponseEntity.ok(studentGroupService.updateStudentGroup(id, studentGroupDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentGroup(@PathVariable final UUID id) {
        if(studentGroupService.deleteStudentGroup(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
