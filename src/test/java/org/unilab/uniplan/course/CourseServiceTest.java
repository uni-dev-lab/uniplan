package org.unilab.uniplan.course;

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

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    private UUID courseId;
    private Course course;
    private CourseDTO courseDTO;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        UUID majorId = UUID.randomUUID();
        courseDTO = new CourseDTO(courseId, majorId, (byte) 2, "bachelor", "regular education");
        course = new Course();
    }

    @Test
    void createCourseShouldReturnAndSavedCourseDTO() {
        when(courseMapper.toEntity(courseDTO)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDTO(course)).thenReturn(courseDTO);

        CourseDTO result = courseService.createCourse(courseDTO);

        assertEquals(courseDTO, result);
        verify(courseRepository).save(course);
    }

    @Test
    void findCourseByIdShouldReturnEmptyIfNotFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Optional<CourseDTO> result = courseService.findCourseById(courseId);

        assertFalse(result.isPresent());
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdShouldReturnCourseDTOIfFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toDTO(course)).thenReturn(courseDTO);

        Optional<CourseDTO> result = courseService.findCourseById(courseId);

        assertTrue(result.isPresent());
        assertEquals(courseDTO, result.get());
    }

    @Test
    void findAllShouldReturnListOfCourseDTOs() {
        List<Course> courseList = List.of(course);
        when(courseRepository.findAll()).thenReturn(courseList);
        when(courseMapper.toDTO(course)).thenReturn(courseDTO);

        List<CourseDTO> result = courseService.findAll();

        assertEquals(1, result.size());
        assertEquals(courseDTO, result.get(0));
    }

    @Test
    void updateCourseShouldReturnUpdatedCourseDTOIfFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        doNothing().when(courseMapper).updateEntityFromDTO(courseDTO, course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDTO(course)).thenReturn(courseDTO);

        Optional<CourseDTO> result = courseService.updateCourse(courseId, courseDTO);

        assertTrue(result.isPresent());
        assertEquals(courseDTO, result.get());
        verify(courseRepository).save(course);
        verify(courseMapper).updateEntityFromDTO(courseDTO, course);
    }

    @Test
    void updateCourseShouldReturnEmptyIfNotFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Optional<CourseDTO> result = courseService.updateCourse(courseId, courseDTO);

        assertFalse(result.isPresent());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void deleteCourseShouldDeleteIfFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        courseService.deleteCourse(courseId);

        verify(courseRepository).delete(course);
    }

    @Test
    void deleteCourseShouldThrowIfNotFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            courseService.deleteCourse(courseId));

        assertTrue(exception.getMessage().contains("Course with ID"));
        verify(courseRepository, never()).delete(any());
    }
}
