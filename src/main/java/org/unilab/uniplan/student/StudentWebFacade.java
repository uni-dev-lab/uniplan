package org.unilab.uniplan.student;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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

    @Transactional
    public void createStudent(final StudentRequestDto request){
        studentValidator.validate(request);
        final Student student = studentMapper.toEntity(request);
        studentService.save(student);
        log.info("created student with ID: {}", student.getId());
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

    @Transactional
    public void updateStudent(final UUID id,
                              final StudentRequestDto request){
        studentValidator.validate(request);
        final Student student = getStudentOrThrow(id);
        studentMapper.updateEntity(request, student);
        studentService.save(student);
        log.info("updated student with ID: {}", student.getId());
    }

    @Transactional
    public void deleteStudent(final UUID id){
        final Student student = getStudentOrThrow(id);
        studentService.delete(student);
        log.info("deleted student with ID: {}", id);
    }

    private Student getStudentOrThrow(final UUID id){
        return studentService.getById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                STUDENT_NOT_FOUND.getMessage(String.valueOf(id))
            ));
    }
}
