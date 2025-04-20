package org.unilab.uniplan.student;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody @NotNull
                                                        @Valid final StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(studentService.createStudent(studentDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudent(@PathVariable
                                                     @NotNull final UUID studentId) {
        return ResponseEntity.ok(studentService.findStudentById(studentId)
                                               .orElseThrow(()-> new StudentNotFoundException(studentId)));
    }
    
    @GetMapping
    public List<StudentDTO> getAllStudents() {
        return studentService.findAll();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable
                                                        @NotNull final UUID studentId, 
                                                    @RequestBody
                                                        @NotNull @Valid final StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.updateStudent(studentId, studentDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable
                                                  @NotNull final UUID studentId) {
        if(studentService.deleteStudent(studentId)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
