package org.unilab.uniplan.student;

import jakarta.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private static final String STUDENT_NOT_FOUND = "Student with ID {0} not found.";

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Transactional
    public StudentDTO createStudent(final StudentDTO studentDTO) {
        final Student student = studentMapper.toEntity(studentDTO);
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
    public Optional<StudentDTO> updateStudent(final UUID id, final StudentDTO studentDTO) {
        return studentRepository.findById(id)
                                .map(existingStudent -> {
                                    studentMapper.updateEntityFromDTO(studentDTO, existingStudent);

                                    return studentMapper.toDTO(studentRepository.save(
                                        existingStudent));
                                });
    }

    @Transactional
    public void deleteStudent(final UUID id) {
        final Student student = studentRepository.findById(id)
                                                 .orElseThrow(() -> new RuntimeException(
                                                     MessageFormat.format(STUDENT_NOT_FOUND, id)));
        studentRepository.delete(student);
    }
}
