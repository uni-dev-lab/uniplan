package org.unilab.uniplan.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.student.dto.StudentDto;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private UUID studentId;
    private StudentDto studentDTO;
    private Student student;

    @BeforeEach
    void beforeAll() {
        studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        studentDTO = new StudentDto(studentId, "Petar", "Petrov", "2301261005", courseId);
        student = new Student();
    }

    @Test
    void createStudentShouldReturnSaveAndReturnStudentDTO() {
        when(studentMapper.toEntity(studentDTO)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDTO);

        StudentDto result = studentService.createStudent(studentDTO);

        assertEquals(studentDTO, result);
        verify(studentRepository).save(student);
    }

    @Test
    void findStudentByIdShouldReturnStudentDTOIfExists() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDTO);

        Optional<StudentDto> result = studentService.findStudentById(studentId);

        assertTrue(result.isPresent());
        assertEquals(studentDTO, result.get());
    }

    @Test
    void findStudentByIdShouldReturnEmptyIfNotExists() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        Optional<StudentDto> result = studentService.findStudentById(studentId);

        assertFalse(result.isPresent());
    }

    @Test
    void findAllShouldReturnMappedStudentDTOList() {
        List<Student> students = List.of(student);
        when(studentRepository.findAll()).thenReturn(students);
        when(studentMapper.toDto(any(Student.class))).thenReturn(studentDTO);

        List<StudentDto> result = studentService.findAll();

        assertEquals(1, result.size());
        assertEquals(studentDTO, result.getFirst());
        verify(studentRepository).findAll();
    }

    @Test
    void updateStudentShouldReturnUpdatedStudentDTOIfExists() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        doNothing().when(studentMapper).updateEntityFromDto(studentDTO, student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDTO);

        Optional<StudentDto> result = studentService.updateStudent(studentId, studentDTO);

        assertTrue(result.isPresent());
        assertEquals(studentDTO, result.get());
        verify(studentRepository).save(student);
    }

    @Test
    void updateStudentShouldReturnEmptyIfNotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        Optional<StudentDto> result = studentService.updateStudent(studentId, studentDTO);

        assertFalse(result.isPresent());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudentShouldRemoveStudentDTOIfExists() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        studentService.deleteStudent(studentId);

        verify(studentRepository).delete(student);
    }

    @Test
    void deleteStudentShouldThrowIfNotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                                                  () -> studentService.deleteStudent(studentId));

        assertTrue(exception.getMessage().contains("Student with ID"));
        verify(studentRepository, never()).delete(any());
    }
}
