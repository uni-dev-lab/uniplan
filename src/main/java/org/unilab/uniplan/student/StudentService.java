package org.unilab.uniplan.student;

import static org.unilab.uniplan.utils.ErrorConstants.STUDENT_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.student.dto.StudentDto;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Transactional
    public StudentDto createStudent(final StudentDto studentDTO) {
        final Student student = studentMapper.toEntity(studentDTO);
        return saveEntityAndConvertToDto(student);
    }

    public StudentDto findStudentById(final UUID id) {
        return studentRepository.findById(id)
                                .map(studentMapper::toDto)
                                .orElseThrow(() -> new ResourceNotFoundException(STUDENT_NOT_FOUND.getMessage(
                                    String.valueOf(id))));
    }

    public List<StudentDto> findAll() {
        return studentRepository.findAll()
                                .stream().map(studentMapper::toDto).toList();
    }

    @Transactional
    public StudentDto updateStudent(final UUID id, final StudentDto studentDTO) {
        return studentRepository.findById(id)
                                .map(existingStudent -> updateEntityAndConvertToDto(
                                    studentDTO,
                                    existingStudent))
                                .orElseThrow(() -> new ResourceNotFoundException(STUDENT_NOT_FOUND.getMessage(
                                    String.valueOf(id))));
    }

    @Transactional
    public void deleteStudent(final UUID id) {
        final Student student = studentRepository.findById(id)
                                                 .orElseThrow(() -> new ResourceNotFoundException(
                                                     STUDENT_NOT_FOUND.getMessage(String.valueOf(id))));
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
