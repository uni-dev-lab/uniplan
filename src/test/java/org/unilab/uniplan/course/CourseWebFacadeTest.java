package org.unilab.uniplan.course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.course.dto.CourseRequestDto;
import org.unilab.uniplan.course.dto.CourseResponseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class CourseWebFacadeTest {

    @Mock
    private CourseService courseService;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private CourseValidator courseValidator;

    @InjectMocks
    private CourseWebFacade courseWebFacade;

    private UUID courseId;
    private UUID majorId;
    private Course course;
    private CourseRequestDto requestDto;
    private CourseResponseDto responseDto;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        majorId = UUID.randomUUID();
        course = new Course();
        requestDto = mock(CourseRequestDto.class);
        responseDto = mock(CourseResponseDto.class);
    }

    @Test
    void createCourseShouldValidateMapAndSaveCourse() {
        when(courseMapper.toEntity(requestDto)).thenReturn(course);

        courseWebFacade.createCourse(requestDto);

        final InOrder inOrder = inOrder(courseValidator, courseMapper, courseService);
        inOrder.verify(courseValidator).validateForCreate(requestDto);
        inOrder.verify(courseMapper).toEntity(requestDto);
        inOrder.verify(courseService).save(course);
    }

    @Test
    void getAllCoursesShouldReturnResponseDtoList() {
        final List<Course> courses = List.of(course);
        final List<CourseResponseDto> responseDtos = List.of(responseDto);

        when(courseService.getAll()).thenReturn(courses);
        when(courseMapper.toResponseDtoList(courses)).thenReturn(responseDtos);

        final List<CourseResponseDto> result = courseWebFacade.getAllCourses();

        assertEquals(responseDtos, result);
        verify(courseService).getAll();
        verify(courseMapper).toResponseDtoList(courses);
    }

    @Test
    void getCourseByIdShouldReturnResponseDtoIfCourseExists() {
        when(courseService.getById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toResponseDto(course)).thenReturn(responseDto);

        final CourseResponseDto result = courseWebFacade.getCourseById(courseId);

        assertEquals(responseDto, result);
        verify(courseService).getById(courseId);
        verify(courseMapper).toResponseDto(course);
    }

    @Test
    void getCourseByIdShouldThrowIfCourseDoesNotExist() {
        when(courseService.getById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> courseWebFacade.getCourseById(courseId));

        verify(courseService).getById(courseId);
        verify(courseMapper, never()).toResponseDto(any(Course.class));
    }

    @Test
    void getCoursesByMajorIdShouldReturnResponseDtoList() {
        final List<Course> courses = List.of(course);
        final List<CourseResponseDto> responseDtos = List.of(responseDto);

        when(courseService.findAllByMajorId(majorId)).thenReturn(courses);
        when(courseMapper.toResponseDtoList(courses)).thenReturn(responseDtos);

        final List<CourseResponseDto> result = courseWebFacade.getCoursesByMajorId(majorId);

        assertEquals(responseDtos, result);
        verify(courseService).findAllByMajorId(majorId);
        verify(courseMapper).toResponseDtoList(courses);
    }

    @Test
    void updateCourseShouldValidateUpdateMapAndSaveCourseIfCourseExists() {
        when(courseService.getById(courseId)).thenReturn(Optional.of(course));

        courseWebFacade.updateCourse(courseId, requestDto);

        final InOrder inOrder = inOrder(courseValidator, courseService, courseMapper);
        inOrder.verify(courseValidator).validateForUpdate(requestDto);
        inOrder.verify(courseService).getById(courseId);
        inOrder.verify(courseMapper).updateEntityFromDto(requestDto, course);
        inOrder.verify(courseService).save(course);
    }

    @Test
    void updateCourseShouldThrowIfCourseDoesNotExist() {
        when(courseService.getById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> courseWebFacade.updateCourse(courseId, requestDto));

        verify(courseValidator).validateForUpdate(requestDto);
        verify(courseService).getById(courseId);
        verify(courseMapper, never()).updateEntityFromDto(any(CourseRequestDto.class), any(Course.class));
        verify(courseService, never()).save(any(Course.class));
    }

    @Test
    void deleteCourseShouldDeleteCourseIfCourseExists() {
        when(courseService.getById(courseId)).thenReturn(Optional.of(course));

        courseWebFacade.deleteCourse(courseId);

        verify(courseService).getById(courseId);
        verify(courseService).delete(course);
    }

    @Test
    void deleteCourseShouldThrowIfCourseDoesNotExist() {
        when(courseService.getById(courseId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                     () -> courseWebFacade.deleteCourse(courseId));

        verify(courseService).getById(courseId);
        verify(courseService, never()).delete(any(Course.class));
    }
}