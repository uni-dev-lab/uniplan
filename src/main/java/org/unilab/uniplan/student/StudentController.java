package org.unilab.uniplan.student;

import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.course.CourseService;
import org.unilab.uniplan.course.dto.CourseDto;
import org.unilab.uniplan.major.MajorService;
import org.unilab.uniplan.major.dto.MajorDto;
import org.unilab.uniplan.student.dto.StudentCourseMajorDto;
import org.unilab.uniplan.student.dto.StudentDto;
import org.unilab.uniplan.student.dto.StudentRequestDto;
import org.unilab.uniplan.student.dto.StudentResponseDto;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Manage students, including faculty numbers and enrollment in course")
public class StudentController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final StudentMapper studentMapper;
    private final MajorService majorService;

    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(@RequestBody @NotNull
                                                            @Valid final StudentRequestDto studentRequestDTO) {
        final StudentDto studentDTO = studentMapper.toInternalDto(studentRequestDTO);
        studentService.createStudent(studentDTO);

        CourseDto courseDto = courseService.findCourseById(studentDTO.courseId());
        MajorDto majorDto = majorService.findMajorById(courseDto.majorId());
        StudentResponseDto student = studentMapper.toResponseDto(studentDTO, courseDto, majorDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudent(@PathVariable
                                                         @NotNull final UUID id) {

        StudentDto studentDTO = studentService.findStudentById(id);
        CourseDto courseDto = courseService.findCourseById(studentDTO.courseId());
        MajorDto majorDto = majorService.findMajorById(courseDto.majorId());
        final StudentResponseDto studentResponseDTO = studentMapper.toResponseDto(studentDTO, courseDto, majorDto);

        return ResponseEntity.ok(studentResponseDTO);
    }

    @GetMapping
    public List<StudentResponseDto> getAllStudents() {
        List<StudentDto> students = studentService.findAll();
        List<StudentResponseDto> responseList = students.stream()
            .map(studentDto -> {
                CourseDto courseDto = courseService.findCourseById(studentDto.courseId());
                MajorDto majorDto = majorService.findMajorById(courseDto.majorId());
                return studentMapper.toResponseDto(studentDto, courseDto, majorDto);
            })
            .toList();

        return responseList;
    }

    @GetMapping("/student-course-major/getStudentCourseMajorInfo")
    public List<StudentCourseMajorDto> getStudentCourseMajorInfo(@RequestParam(required = false) @Size(max = 100) final String firstName,
                                                                 @RequestParam(required = false) @Size(max = 100) final String lastName,
                                                                 @RequestParam(required = false) final String facultyNumber,
                                                                 @RequestParam(required = false) @Size(max = 200) final String majorName){
        return studentService.findStudentCourseMajorInfo(firstName, lastName, facultyNumber, majorName);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable
                                                            @NotNull final UUID id,
                                                            @RequestBody
                                                            @NotNull @Valid final StudentRequestDto studentRequestDTO) {
        final StudentDto studentDTO = studentMapper.toInternalDto(studentRequestDTO);
        StudentDto studentDto = studentService.updateStudent(id, studentDTO);
        CourseDto courseDto = courseService.findCourseById(studentDto.courseId());
        MajorDto majorDto = majorService.findMajorById(courseDto.majorId());
        return ResponseEntity.ok(studentMapper.toResponseDto(studentDTO, courseDto, majorDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable
                                              @NotNull final UUID id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
