package org.unilab.uniplan.course;

import static org.unilab.uniplan.utils.ErrorConstants.COURSE_NOT_FOUND;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.course.dto.CourseRequestDto;
import org.unilab.uniplan.course.dto.CourseResponseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CourseWebFacade {

    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final CourseValidator courseValidator;

    @Transactional
    public void createCourse(final CourseRequestDto requestDto) {
        courseValidator.validateForCreate(requestDto);

        final Course course = courseMapper.toEntity(requestDto);
        courseService.save(course);

        log.info("Created course with ID: {}", course.getId());
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getAllCourses() {
        return courseMapper.toResponseDtoList(courseService.getAll());
    }

    @Transactional(readOnly = true)
    public CourseResponseDto getCourseById(final UUID id) {
        final Course course = getCourseOrThrow(id);

        return courseMapper.toResponseDto(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getCoursesByMajorId(final UUID majorId) {
        return courseMapper.toResponseDtoList(courseService.findAllByMajorId(majorId));
    }

    @Transactional
    public void updateCourse(final UUID id,
                             final CourseRequestDto requestDto) {
        courseValidator.validateForUpdate(requestDto);

        final Course course = getCourseOrThrow(id);
        courseMapper.updateEntityFromDto(requestDto, course);
        courseService.save(course);

        log.info("Updated course with ID: {}", id);
    }

    @Transactional
    public void deleteCourse(final UUID id) {
        final Course course = getCourseOrThrow(id);

        courseService.delete(course);

        log.info("Deleted course with ID: {}", id);
    }

    private Course getCourseOrThrow(final UUID id) {
        return courseService.getById(id)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                COURSE_NOT_FOUND.getMessage(String.valueOf(id))
                            ));
    }
}