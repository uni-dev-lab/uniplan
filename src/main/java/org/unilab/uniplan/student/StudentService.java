package org.unilab.uniplan.student;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.exception.StudentNotFoundException;
import org.unilab.uniplan.student.dto.StudentCourseMajorDto;
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
                                .orElseThrow(() -> new StudentNotFoundException(id));
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
                                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Transactional
    public void deleteStudent(final UUID id) {
        final Student student = studentRepository.findById(id)
                                                 .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
    }

    public List<StudentCourseMajorDto> findStudentCourseMajorInfo(final String firstName, final String lastName,
                                                                  final String facultyNumber, final String majorName){
        return studentRepository.searchStudents(firstName, lastName, facultyNumber, majorName);
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
