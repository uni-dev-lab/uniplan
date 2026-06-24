package org.unilab.uniplan.student;

import static org.unilab.uniplan.utils.ErrorConstants.STUDENT_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.student.dto.StudentCourseMajorDto;
import org.unilab.uniplan.student.dto.StudentDto;
import org.unilab.uniplan.student.dto.StudentRequestDto;
import org.unilab.uniplan.student.dto.StudentResponseDto;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto request) {
        StudentDto studentDto = studentMapper.toInternalDto(request);
        Student saved = studentRepository.save(studentMapper.toEntity(studentDto));
        return studentRepository.findStudentWithDetailsById(saved.getId())
                                .map(studentMapper::toResponseDto)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                    STUDENT_NOT_FOUND.getMessage(String.valueOf(saved.getId()))));
    }

    public StudentResponseDto findStudentById(final UUID id) {
        return studentRepository.findStudentWithDetailsById(id)
                                .map(studentMapper::toResponseDto)
                                .orElseThrow(() -> new ResourceNotFoundException(STUDENT_NOT_FOUND.getMessage(
                                    String.valueOf(id))));
    }

    public List<StudentDto> findAll() {
        return studentRepository.findAll()
                                .stream().map(studentMapper::toDto).toList();
    }

    public List<StudentResponseDto> findAllStudentsWithDetails() {
        return studentMapper.toResponseDtoList(
            studentRepository.searchStudents("", "", "", "")
        );
    }

    public List<StudentCourseMajorDto> findStudentCourseMajorInfo(final String firstName, final String lastName,
                                                                  final String facultyNumber, final String majorName){
        return studentRepository.searchStudents(firstName, lastName, facultyNumber, majorName);
    }

    @Transactional
    public StudentResponseDto updateStudent(final UUID id, final StudentRequestDto request) {
        Student existing = studentRepository.findById(id)
                                            .orElseThrow(() -> new ResourceNotFoundException(STUDENT_NOT_FOUND.getMessage(String.valueOf(id))));

        StudentDto studentDto = studentMapper.toInternalDto(request);
        studentMapper.updateEntityFromDto(studentDto, existing);

        Course course = new Course();
        course.setId(studentDto.courseId());
        existing.setCourse(course);

        studentRepository.save(existing);

        return studentRepository.findStudentWithDetailsById(id)
                                .map(studentMapper::toResponseDto)
                                .orElseThrow(() -> new ResourceNotFoundException(STUDENT_NOT_FOUND.getMessage(String.valueOf(id))));
    }

    @Transactional
    public void deleteStudent(final UUID id) {
        final Student student = studentRepository.findById(id)
                                                 .orElseThrow(() -> new ResourceNotFoundException(
                                                     STUDENT_NOT_FOUND.getMessage(String.valueOf(id))));
        studentRepository.delete(student);
    }
}
