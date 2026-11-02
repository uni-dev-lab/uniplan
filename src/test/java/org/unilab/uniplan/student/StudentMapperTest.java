package org.unilab.uniplan.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.major.Major;
import org.unilab.uniplan.student.dto.StudentRequestDto;
import org.unilab.uniplan.student.dto.StudentResponseDto;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.AssertionsKt.assertNull;

@ExtendWith(MockitoExtension.class)
class StudentMapperTest {

    private final StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    private UUID studentId;
    private UUID courseId;
    private UUID majorId;
    private Student student;
    private StudentRequestDto requestDto;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        majorId = UUID.randomUUID();

        requestDto = new StudentRequestDto("Petar", "Petrov", "2301261005", courseId);

        Major major = new Major();
        major.setId(majorId);
        major.setMajorName("Software Engineering");

        Course course = new Course();
        course.setId(courseId);
        course.setMajor(major);
        course.setCourseType("FULL_TIME");
        course.setCourseSubtype("редовно");
        course.setCourseYear((byte) 2);

        student = new Student();
        student.setId(studentId);
        student.setFirstName("Petar");
        student.setLastName("Petrov");
        student.setFacultyNumber("2301261005");
        student.setCourse(course);
    }

    @Test
    void toEntity_ShouldMapFieldsCorrectly() {
        Student result = studentMapper.toEntity(requestDto);

        assertNull(result.getId());
        assertEquals("Petar", result.getFirstName());
        assertEquals("Petrov", result.getLastName());
        assertEquals("2301261005", result.getFacultyNumber());
        assertEquals(courseId, result.getCourse().getId());
    }

    @Test
    void toEntity_ShouldIgnoreId() {
        Student result = studentMapper.toEntity(requestDto);

        assertNull(result.getId());
    }

    @Test
    void updateEntity_ShouldUpdateFieldsCorrectly() {
        UUID newCourseId = UUID.randomUUID();
        StudentRequestDto updateDto = new StudentRequestDto("Ivan", "Ivanov", "1234567890", newCourseId);

        studentMapper.updateEntity(updateDto, student);

        assertEquals("Ivan", student.getFirstName());
        assertEquals("Ivanov", student.getLastName());
        assertEquals("1234567890", student.getFacultyNumber());
        assertEquals(courseId, student.getCourse().getId());
    }

    @Test
    void updateEntity_ShouldNotAlterStudentId() {
        studentMapper.updateEntity(requestDto, student);

        assertEquals(studentId, student.getId());
    }

    @Test
    void toResponseDto_ShouldMapFieldsCorrectly() {
        StudentResponseDto result = studentMapper.toResponseDto(student);

        assertEquals(studentId, result.id());
        assertEquals("Petar Petrov", result.name());
        assertEquals("2301261005", result.facultyNumber());
        assertEquals(majorId, result.majorId()); // adjust if majorId comes from major.id
        assertEquals("Software Engineering", result.majorName());
        assertEquals("FULL_TIME", result.courseType());
        assertEquals("редовно", result.courseSubtype());
        assertEquals((byte) 2, result.courseYear());
    }

    @Test
    void toResponseDtoList_ShouldMapAllElements() {
        List<StudentResponseDto> result = studentMapper.toResponseDtoList(List.of(student));

        assertEquals(1, result.size());
        assertEquals(studentId, result.getFirst().id());
    }

    @Test
    void toResponseDtoList_ShouldReturnEmptyForEmptyInput() {
        List<StudentResponseDto> result = studentMapper.toResponseDtoList(List.of());

        assertTrue(result.isEmpty());
    }
}
