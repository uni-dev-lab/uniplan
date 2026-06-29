package org.unilab.uniplan.student;

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
import org.unilab.uniplan.student.dto.StudentRequestDto;
import org.unilab.uniplan.student.dto.StudentResponseDto;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Manage students, including faculty numbers and enrollment in course")
public class StudentController {

    private final StudentWebFacade studentWebFacade;

    @PostMapping
    public ResponseEntity<Void> createStudent(@RequestBody @NotNull @Valid final StudentRequestDto studentRequestDTO) {
        studentWebFacade.createStudent(studentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable
                                                         @NotNull final UUID id) {
        return ResponseEntity.ok(studentWebFacade.getStudentById(id));
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        return ResponseEntity.ok(studentWebFacade.getAllStudents());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable
                                                            @NotNull final UUID id,
                                                            @RequestBody
                                                            @NotNull @Valid final StudentRequestDto studentRequestDTO) {
        studentWebFacade.updateStudent(id, studentRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable
                                              @NotNull final UUID id) {
        studentWebFacade.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
