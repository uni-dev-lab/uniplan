package org.unilab.uniplan.course;

import static org.unilab.uniplan.utils.ErrorConstants.COURSE_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.course.dto.CourseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional
    public CourseDto createCourse(final CourseDto courseDTO) {
        final Course course = courseMapper.toEntity(courseDTO);
        return saveEntityAndConvertToDto(course);
    }

    public CourseDto findCourseById(final UUID id) {
        return courseRepository.findById(id)
                               .map(courseMapper::toDto)
                               .orElseThrow(() -> new ResourceNotFoundException(COURSE_NOT_FOUND.getMessage(
                                   id.toString())));
    }

    public List<CourseDto> findAll() {
        return courseRepository.findAll()
                               .stream().map(courseMapper::toDto).toList();
    }

    @Transactional
    public CourseDto updateCourse(final UUID id, final CourseDto courseDTO) {
        return courseRepository.findById(id).map(
            existingCourse -> updateEntityAndConvertToDto(
                courseDTO,
                existingCourse))
                               .orElseThrow(() -> new ResourceNotFoundException(COURSE_NOT_FOUND.getMessage(
                                   id.toString())));
    }

    @Transactional
    public void deleteCourse(final UUID id) {
        final Course course = courseRepository.findById(id)
                                              .orElseThrow(() -> new ResourceNotFoundException(
                                                  COURSE_NOT_FOUND.getMessage(id.toString())));
        courseRepository.delete(course);
    }

    private CourseDto updateEntityAndConvertToDto(final CourseDto dto,
                                                  final Course entity) {
        courseMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private CourseDto saveEntityAndConvertToDto(final Course entity) {
        final Course savedEntity = courseRepository.save(entity);
        return courseMapper.toDto(savedEntity);
    }
}
