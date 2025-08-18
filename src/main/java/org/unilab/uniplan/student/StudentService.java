package org.unilab.uniplan.student;

import jakarta.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.student.dto.StudentDto;

@Service
@RequiredArgsConstructor
public class StudentService {

    private static final String STUDENT_NOT_FOUND = "Student with ID {0} not found.";

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Transactional
    public StudentDto createStudent(final StudentDto studentDTO) {
        final Student student = studentMapper.toEntity(studentDTO);
        return saveEntityAndConvertToDto(student);
    }

    public Optional<StudentDto> findStudentById(final UUID id) {
        return studentRepository.findById(id)
                                .map(studentMapper::toDto);
    }

    public List<StudentDto> findAll() {
        return studentRepository.findAll()
                                .stream().map(studentMapper::toDto).toList();
    }

    @Transactional
    public Optional<StudentDto> updateStudent(final UUID id, final StudentDto studentDTO) {
        return studentRepository.findById(id)
                                .map(existingStudent -> updateEntityAndConvertToDto(
                                    studentDTO,
                                    existingStudent));
    }

    @Transactional
    public void deleteStudent(final UUID id) {
        final Student student = studentRepository.findById(id)
                                                 .orElseThrow(() -> new RuntimeException(
                                                     MessageFormat.format(STUDENT_NOT_FOUND, id)));
        studentRepository.delete(student);
    }

    private StudentDto updateEntityAndConvertToDto(final StudentDto dto,
                                                   final Student entity) {
        studentMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private StudentDto saveEntityAndConvertToDto(final Student entity) {
        final Student savedEntity = studentRepository.save(entity);
        return studentMapper.toDto(savedEntity);
    }
}
