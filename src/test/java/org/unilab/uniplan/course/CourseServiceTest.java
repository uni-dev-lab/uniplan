package org.unilab.uniplan.course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.unilab.uniplan.course.dto.CourseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private UUID courseId;
    private UUID majorId;
    private Course course;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        majorId = UUID.randomUUID();
        course = new Course();
    }


    @Test
    void saveShouldSaveCourse() {
        courseService.save(course);

        verify(courseRepository).save(course);
    }


    @Test
    void findAllByMajorIdShouldReturnListOfCourses() {
        final List<Course> courses = List.of(course);

        when(courseRepository.findAllByMajorId(majorId)).thenReturn(List.of(course));

        final List<Course> result =  courseService.findAllByMajorId(majorId);

        assertEquals(courses, result);
        verify(courseRepository).findAllByMajorId(majorId);    }

    @Test
    void findAllByMajorIdShouldReturnEmptyList() {

        when(courseRepository.findAllByMajorId(majorId)).thenReturn(List.of());

        final List<Course> result =  courseService.findAllByMajorId(majorId);

        assertTrue(result.isEmpty());
        verify(courseRepository).findAllByMajorId(majorId);
    }

    @Test
    void getByIdShouldReturnEmptyOptionalIfCourseDoesNotExist() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        final Optional<Course> result = courseService.getById(courseId);

        assertEquals(Optional.empty(), result);
        verify(courseRepository).findById(courseId);
    }

    @Test
    void getByIdShouldReturnCourseOptionalIfCourseExists() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        final Optional<Course> result = courseService.getById(courseId);

        assertEquals(Optional.of(course), result);
        verify(courseRepository).findById(courseId);
    }

    @Test
    void getAllShouldReturnListOfCourses() {
        final List<Course> courseList = List.of(course);
        when(courseRepository.findAll()).thenReturn(courseList);

        final List<Course> result = courseService.getAll();

        assertEquals(courseList, result);
        verify(courseRepository).findAll();
    }

    @Test
    void deleteCourseShouldDeleteIfFound() {
        courseService.delete(course);

        verify(courseRepository).delete(course);
    }
}
