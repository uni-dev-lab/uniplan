package org.unilab.uniplan.student;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.course.CourseNotFoundException;
import org.unilab.uniplan.course.CourseService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final CourseService courseService;
    
    @Transactional
    public StudentDTO createStudent(final StudentDTO studentDTO) {
        Course course = courseService.findById(studentDTO.courseId())
                                     .orElseThrow(() ->new CourseNotFoundException(studentDTO.courseId()));
        Student student = studentMapper.toEntity(studentDTO);
        student.setCreatedAt();
        return studentMapper.toDTO(studentRepository.save(student));
    }
    
    public Optional<StudentDTO> findStudentById(final UUID id) {
        return studentRepository.findById(id)
            .map(studentMapper::toDTO);
    }

    public Optional<Student> findById(final UUID id) {
        return studentRepository.findById(id);
    }
    
    public List<StudentDTO> findAll() {
        return studentRepository.findAll()
                                .stream().map(studentMapper::toDTO).toList();
    }
    @Transactional
    public StudentDTO updateStudent(final UUID id, final StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                                           .orElseThrow(()->new StudentNotFoundException(id));
        Course course = courseService.findById(studentDTO.courseId())
                                     .orElseThrow(()->new StudentNotFoundException(studentDTO.courseId()));
        studentMapper.updateEntityFromDTO(studentDTO, student);
        student.setUpdatedAt();
        return studentMapper.toDTO(studentRepository.save(student));
    }
    @Transactional
    public boolean deleteStudent(final UUID studentId){
        if (studentRepository.existsById(studentId)) {
            studentRepository.deleteById(studentId);
            return true;
        }   
        return false;
    }
}
