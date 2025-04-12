package org.unilab.uniplan.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.course.CourseService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final CourseService courseService;

    @Autowired
    public StudentService (final StudentRepository studentRepository,
                           final StudentMapper studentMapper,
                           final CourseService courseService) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.courseService = courseService;
    }
    
    @Transactional
    public StudentDTO createStudent(final StudentDTO studentDTO) {
        Course course = courseService.findById(studentDTO.courseId())
                                     .orElseThrow(() ->new RuntimeException("Course with id " + studentDTO.courseId() + " doesn't exists"));
        Student student = new Student(course, studentDTO.facultyNumber());
        student.setFirstName(studentDTO.firstName());
        student.setLastName(studentDTO.lastName());
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
                                           .orElseThrow(()->new RuntimeException("Student with id " + studentDTO.id() + " doesn't exists"));
        Course course = courseService.findById(studentDTO.courseId())
                                     .orElseThrow(()->new RuntimeException("Course with id " + studentDTO.courseId() + " doesn't exists"));
        student.setFirstName(studentDTO.firstName());
        student.setLastName(studentDTO.lastName());
        student.setFacultyNumber(studentDTO.facultyNumber());
        student.setCourse(course);
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
