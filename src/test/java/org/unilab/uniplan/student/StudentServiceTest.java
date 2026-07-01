package org.unilab.uniplan.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student();
    }

    @Test
    void save_ShouldDelegateToRepository() {
        studentService.save(student);
        verify(studentRepository).save(student);
    }

    @Test
    void getById_ShouldReturnStudent_IfExists() {
        UUID id = UUID.randomUUID();
        when(studentRepository.findByIdWithCourseAndMajor(id)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.getById(id);

        assertTrue(result.isPresent());
        assertEquals(student, result.get());
    }

    @Test
    void getById_ShouldReturnEmpty_IfNotExists() {
        UUID id = UUID.randomUUID();
        when(studentRepository.findByIdWithCourseAndMajor(id)).thenReturn(Optional.empty());

        assertTrue(studentService.getById(id).isEmpty());
    }

    @Test
    void getAll_ShouldReturnAllStudents() {
        when(studentRepository.findAllWithCourseAndMajor()).thenReturn(List.of(student));

        assertEquals(1, studentService.getAll().size());
    }

    @Test
    void delete_ShouldDelegateToRepository() {
        studentService.delete(student);
        verify(studentRepository).delete(student);
    }
}
