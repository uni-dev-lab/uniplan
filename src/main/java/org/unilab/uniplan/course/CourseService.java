package org.unilab.uniplan.course;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    
    @Transactional
    public CourseDTO createCourse(final CourseDTO courseDTO) {
        final Course course = courseMapper.toEntity(courseDTO);
        return courseMapper.toDTO(courseRepository.save(course));
    }
    
    public Optional<CourseDTO> findCourseById (final UUID id) {
        return courseRepository.findById(id)
                               .map(courseMapper::toDTO);
    }

    public Optional<Course> findById (final UUID id) {
        return courseRepository.findById(id);
    }
    
    public List<CourseDTO> findAll () {
        return courseRepository.findAll()
            .stream().map(courseMapper::toDTO).toList();
    }
    @Transactional
    public CourseDTO updateCourse(final UUID id, final CourseDTO courseDTO) {
        final Course course  = courseRepository.findById(id)
                                           .orElseThrow(()->new CourseNotFoundException(id));
        courseMapper.updateEntityFromDTO(courseDTO, course);
        return courseMapper.toDTO(courseRepository.save(course));
    }
    @Transactional
    public boolean deleteCourse(final UUID id){
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
