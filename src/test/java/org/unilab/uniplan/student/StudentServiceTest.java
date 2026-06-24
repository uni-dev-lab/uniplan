package org.unilab.uniplan.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.student.dto.StudentCourseMajorDto;
import org.unilab.uniplan.student.dto.StudentDto;
import org.unilab.uniplan.student.dto.StudentRequestDto;
import org.unilab.uniplan.student.dto.StudentResponseDto;

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
    private StudentRequestDto studentRequestDto;
    private StudentCourseMajorDto studentCourseMajorDto;
    private StudentResponseDto studentResponseDto;

    @BeforeEach
    void beforeEach() {
        studentId = UUID.randomUUID();
        UUID courseId = UUID.randomUUID();
        UUID majorId = UUID.randomUUID();
        studentDTO = new StudentDto(studentId, "Petar", "Petrov", "2301261005", courseId);
        studentRequestDto = new StudentRequestDto("Petar", "Petrov", "2301261005", courseId);
        studentCourseMajorDto = new StudentCourseMajorDto(
            studentId, "Petar", "Petrov", "2301261005",
            courseId, "bachelor", "regular", (byte) 2,
            majorId, "Informatics"
        );
        studentResponseDto = new StudentResponseDto(
            studentId, "Petar Petrov", "2301261005", majorId, "Informatics", "bachelor", "regular",
            (byte) 2
        );
        student = new Student();
        student.setId(studentId);
    }

    @Test
    void createStudentShouldSaveAndReturnStudentResponseDto() {
        when(studentMapper.toInternalDto(studentRequestDto)).thenReturn(studentDTO);
        when(studentMapper.toEntity(studentDTO)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentRepository.findStudentWithDetailsById(studentId)).thenReturn(Optional.of(studentCourseMajorDto));
        when(studentMapper.toResponseDto(studentCourseMajorDto)).thenReturn(studentResponseDto);

        StudentResponseDto result = studentService.createStudent(studentRequestDto);

        assertEquals(studentResponseDto, result);
        verify(studentRepository).save(student);
        verify(studentRepository).findStudentWithDetailsById(studentId);
    }

    @Test
    void findStudentByIdShouldReturnStudentResponseDtoIfExists() {
        when(studentRepository.findStudentWithDetailsById(studentId)).thenReturn(Optional.of(studentCourseMajorDto));
        when(studentMapper.toResponseDto(studentCourseMajorDto)).thenReturn(studentResponseDto);

        StudentResponseDto result = studentService.findStudentById(studentId);

        assertEquals(studentResponseDto, result);
    }

    @Test
    void findStudentByIdShouldReturnEmptyIfNotExists() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> studentService.findStudentById(studentId));

        assertTrue(exception.getMessage().contains(String.valueOf(studentId)));
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
    void updateStudentShouldReturnUpdatedStudentResponseDtoIfExists() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toInternalDto(studentRequestDto)).thenReturn(studentDTO);
        doNothing().when(studentMapper).updateEntityFromDto(studentDTO, student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentRepository.findStudentWithDetailsById(studentId)).thenReturn(Optional.of(studentCourseMajorDto));
        when(studentMapper.toResponseDto(studentCourseMajorDto)).thenReturn(studentResponseDto);

        StudentResponseDto result = studentService.updateStudent(studentId, studentRequestDto);

        assertEquals(studentResponseDto, result);
        verify(studentRepository).save(student);
        verify(studentRepository).findStudentWithDetailsById(studentId);
    }

    @Test
    void updateStudentShouldThrowIfNotFound() {
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> studentService.updateStudent(studentId, studentRequestDto));

        assertTrue(exception.getMessage().contains(String.valueOf(studentId)));
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

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                  () -> studentService.deleteStudent(studentId));

        assertTrue(exception.getMessage().contains(String.valueOf(studentId)));
        verify(studentRepository, never()).delete(any());
    }
}
