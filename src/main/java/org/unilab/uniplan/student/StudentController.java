package org.unilab.uniplan.student;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.Size;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.unilab.uniplan.student.dto.StudentCourseMajorDto;
import org.unilab.uniplan.student.dto.StudentDto;
import org.unilab.uniplan.student.dto.StudentRequestDto;
import org.unilab.uniplan.student.dto.StudentResponseDto;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(@RequestBody @NotNull
                                                            @Valid final StudentRequestDto studentRequestDTO) {
        final StudentDto studentDTO = studentMapper.toInternalDto(studentRequestDTO);
        studentService.createStudent(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(studentMapper.toResponseDto(studentDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudent(@PathVariable
                                                         @NotNull final UUID id) {
        final StudentResponseDto studentResponseDTO = studentMapper.toResponseDto(studentService.findStudentById(
                                                                                                    id));

        return ResponseEntity.ok(studentResponseDTO);
    }

    @GetMapping
    public List<StudentResponseDto> getAllStudents() {
        return studentMapper.toResponseDtoList(studentService.findAll());
    }

    @GetMapping("/student-course-major/getStudentCourseMajorInfo")
    public List<StudentCourseMajorDto> getStudentCourseMajorInfo(@RequestParam(required = false)
                                                                         @Size(max = 100)
                                                                         final String firstName,
                                                                 @RequestParam(required = false)
                                                                         @Size(max = 100)
                                                                         final String lastName,
                                                                 @RequestParam(required = false)
                                                                         final String facultyNumber,
                                                                 @RequestParam(required = false)
                                                                         @Size(max = 200)
                                                                         final String majorName){
        return studentService.findStudentCourseMajorInfo(firstName, lastName, facultyNumber,
                                                             majorName);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable
                                                            @NotNull final UUID id,
                                                            @RequestBody
                                                            @NotNull @Valid final StudentRequestDto studentRequestDTO) {
        final StudentDto studentDTO = studentMapper.toInternalDto(studentRequestDTO);
        studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(studentMapper.toResponseDto(studentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable
                                              @NotNull final UUID id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
