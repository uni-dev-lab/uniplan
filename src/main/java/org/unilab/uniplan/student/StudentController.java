package org.unilab.uniplan.student;

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
import org.unilab.uniplan.student.dto.StudentDto;
import org.unilab.uniplan.student.dto.StudentRequestDto;
import org.unilab.uniplan.student.dto.StudentResponseDto;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private static final String STUDENT_NOT_FOUND = "Student with ID {0} not found.";

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(@RequestBody @NotNull
                                                            @Valid final StudentRequestDto studentRequestDTO) {
        final StudentDto studentDTO = studentMapper.toInternalDTO(studentRequestDTO);
        studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(studentMapper.toResponseDTO(studentDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudent(@PathVariable
                                                         @NotNull final UUID id) {
        final StudentResponseDto studentResponseDTO = studentMapper.toResponseDTO(studentService.findStudentById(
                                                                                                    id)
                                                                                                .orElseThrow(
                                                                                                    () -> new ResponseStatusException(
                                                                                                        HttpStatus.NOT_FOUND,
                                                                                                        MessageFormat.format(
                                                                                                            STUDENT_NOT_FOUND,
                                                                                                            id))));

        return ResponseEntity.ok(studentResponseDTO);
    }

    @GetMapping
    public List<StudentResponseDto> getAllStudents() {
        return studentMapper.toResponseDTOList(studentService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable
                                                            @NotNull final UUID id,
                                                            @RequestBody
                                                            @NotNull @Valid final StudentRequestDto studentRequestDTO) {
        final StudentDto studentDTO = studentMapper.toInternalDTO(studentRequestDTO);
        studentService.updateStudent(id, studentDTO).orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                MessageFormat.format(
                    STUDENT_NOT_FOUND,
                    id)));
        return ResponseEntity.ok(studentMapper.toResponseDTO(studentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable
                                              @NotNull final UUID id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
