package org.unilab.uniplan.student;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.course.CourseService;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.student.dto.StudentRequestDto;
import org.unilab.uniplan.student.dto.StudentResponseDto;
import java.util.List;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.STUDENT_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class StudentWebFacade {

    private final StudentMapper studentMapper;
    private final StudentService studentService;
    private final StudentValidator studentValidator;
    private final CourseService courseService;

    @Transactional
    public void createStudent(final StudentRequestDto request){
        final Student student = studentMapper.toEntity(request);
        studentValidator.validate(student);
        studentService.save(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponseDto> getAllStudents(){
        return studentMapper.toResponseDtoList(studentService.getAll());
    }

    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(final UUID id){
        final Student student = getStudentOrThrow(id);
        return studentMapper.toResponseDto(student);
    }

    private Student getStudentOrThrow(final UUID id){
        return studentService.getById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                STUDENT_NOT_FOUND.getMessage(String.valueOf(id))
            ));
    }

    @Transactional
    public void updateStudent(final UUID id,
                              final StudentRequestDto request){
        final Student student = getStudentOrThrow(id);

        studentValidator.validate(student);
        studentMapper.updateEntity(request, student);
        Course updatedCourse = courseService.getReference(request.courseId());

        student.setCourse(updatedCourse);

        studentService.save(student);
        log.info("updated student with ID: {}", student.getId());
    }

    @Transactional
    public void deleteStudent(final UUID id){
        final Student student = getStudentOrThrow(id);
        studentService.delete(student);
        log.info("deleted student with ID: {}", id);
    }
}
