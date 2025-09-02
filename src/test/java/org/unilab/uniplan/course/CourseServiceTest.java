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

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    private UUID courseId;
    private UUID majorId;
    private Course course;
    private CourseDto courseDTO;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        majorId = UUID.randomUUID();
        courseDTO = new CourseDto(courseId, majorId, (byte) 2, "bachelor", "regular education");
        course = new Course();
    }


    @Test
    void createCourseShouldReturnAndSavedCourseDTO() {
        when(courseMapper.toEntity(courseDTO)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        CourseDto result = courseService.createCourse(courseDTO);

        assertEquals(courseDTO, result);
        verify(courseRepository).save(course);
    }


    @Test
    void findAllByMajorIdShouldReturnListOfCourses() {


        when(courseRepository.findAllByMajorId(majorId)).thenReturn(List.of(course));
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        List<CourseDto> result =  courseService.findAllByMajorId(majorId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("bachelor", result.getFirst().courseType());
    }

    @Test
    void findAllByMajorIdShouldReturnEmptyList() {

        when(courseRepository.findAllByMajorId(majorId)).thenReturn(List.of());

        assertTrue(courseService.findAllByMajorId(majorId).isEmpty());

        verify(courseRepository).findAllByMajorId(majorId);
    }

    @Test
    void findCourseByIdShouldReturnEmptyIfNotFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> courseService.findCourseById(courseId));

        assertTrue(exception.getMessage().contains(String.valueOf(courseId)));
    }

    @Test
    void findByIdShouldReturnCourseDTOIfFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        CourseDto result = courseService.findCourseById(courseId);

        assertEquals(courseDTO, result);
    }

    @Test
    void findAllShouldReturnListOfCourseDTOs() {
        List<Course> courseList = List.of(course);
        when(courseRepository.findAll()).thenReturn(courseList);
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        List<CourseDto> result = courseService.findAll();

        assertEquals(1, result.size());
        assertEquals(courseDTO, result.getFirst());
    }

    @Test
    void updateCourseShouldReturnUpdatedCourseDTOIfFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        doNothing().when(courseMapper).updateEntityFromDto(courseDTO, course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        CourseDto result = courseService.updateCourse(courseId, courseDTO);

        assertEquals(courseDTO, result);
        verify(courseRepository).save(course);
        verify(courseMapper).updateEntityFromDto(courseDTO, course);
    }

    @Test
    void updateCourseShouldReturnEmptyIfNotFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> courseService.updateCourse(courseId, courseDTO));

        assertTrue(exception.getMessage().contains(String.valueOf(courseId)));
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

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            courseService.deleteCourse(courseId));

        assertTrue(exception.getMessage().contains(String.valueOf(courseId)));
        verify(courseRepository, never()).delete(any());
    }
}
