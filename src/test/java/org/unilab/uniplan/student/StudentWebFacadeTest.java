package org.unilab.uniplan.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.student.dto.StudentRequestDto;
import org.unilab.uniplan.student.dto.StudentResponseDto;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentWebFacadeTest {

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private StudentService studentService;

    @Mock
    private StudentValidator studentValidator;

    @InjectMocks
    private StudentWebFacade studentWebFacade;

    private UUID studentId;
    private UUID courseId;
    private Student student;
    private StudentRequestDto requestDto;
    private StudentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        student = new Student();
        requestDto = new StudentRequestDto("Petar", "Petrov", "2301261005", courseId);
        responseDto = new StudentResponseDto(studentId, "Petar", "Petrov", "2301261005", courseId);
    }

    @Test
    void createStudentShouldMapValidateAndSave() {
        when(studentMapper.toEntity(requestDto)).thenReturn(student);

        studentWebFacade.createStudent(requestDto);

        InOrder order = inOrder(studentMapper, studentValidator, studentService);
        order.verify(studentMapper).toEntity(requestDto);
        order.verify(studentValidator).validate(student);
        order.verify(studentService).save(student);
    }

    @Test
    void updateStudentShouldThrowIfNotFound() {
        when(studentService.getById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> studentWebFacade.updateStudent(studentId, requestDto));

        verify(studentService, never()).save(any());
    }

    @Test
    void updateStudentShouldValidateAndSave() {
        when(studentService.getById(studentId)).thenReturn(Optional.of(student));

        studentWebFacade.updateStudent(studentId, requestDto);

        InOrder order = inOrder(studentValidator, studentMapper, studentService);
        order.verify(studentValidator).validate(student);
        order.verify(studentMapper).updateEntity(requestDto, student);
        order.verify(studentService).save(student);
    }

    @Test
    void getStudentByIdShouldReturnMappedDto() {
        when(studentService.getById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toResponseDto(student)).thenReturn(responseDto);

        assertEquals(responseDto, studentWebFacade.getStudentById(studentId));
    }

    @Test
    void getStudentByIdShouldThrowIfNotFound() {
        when(studentService.getById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> studentWebFacade.getStudentById(studentId));
    }

    @Test
    void deleteStudentShouldDelegateIfExists() {
        when(studentService.getById(studentId)).thenReturn(Optional.of(student));

        studentWebFacade.deleteStudent(studentId);

        verify(studentService).delete(student);
    }

    @Test
    void deleteStudentShouldThrowIfNotFound() {
        when(studentService.getById(studentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> studentWebFacade.deleteStudent(studentId));

        verify(studentService, never()).delete(any());
    }
}
