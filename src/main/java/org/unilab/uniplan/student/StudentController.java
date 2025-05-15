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
import org.springframework.web.server.ResponseStatusException;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final StudentMapper studentMapper;
    
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody @NotNull
                                                        @Valid final StudentRequestDTO studentRequestDTO) {
        final StudentDTO studentDTO = studentMapper.toInternalDTO(studentRequestDTO);
        studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(studentMapper.toResponseDTO(studentDTO));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudent(@PathVariable
                                                     @NotNull final UUID id) {
       final StudentResponseDTO studentResponseDTO = studentMapper.toResponseDTO(studentService.findStudentById(id)
                                            .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                MessageFormat.format("STUDENT_NOT_FOUND", id))));
        return ResponseEntity.ok(studentResponseDTO);
    }
    
    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        return studentMapper.toResponseDTOList(studentService.findAll());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable
                                                        @NotNull final UUID id, 
                                                    @RequestBody
                                                        @NotNull @Valid final StudentRequestDTO studentRequestDTO) {
        final StudentDTO studentDTO = studentMapper.toInternalDTO(studentRequestDTO);
        studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(studentMapper.toResponseDTO(studentDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable
                                                  @NotNull final UUID id) {
        if(studentService.deleteStudent(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
