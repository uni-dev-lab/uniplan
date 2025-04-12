package org.unilab.uniplan.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(final StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody final StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.createStudent(studentDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable final UUID studentId) {
        return ResponseEntity.ok(studentService.findStudentById(studentId)
                                               .orElseThrow(()-> new RuntimeException("Student not found")));
    }
    
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable final UUID studentId, @RequestBody final StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(studentId, studentDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable final UUID studentId) {
        if(studentService.deleteStudent(studentId)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
