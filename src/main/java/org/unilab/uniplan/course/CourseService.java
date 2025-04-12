package org.unilab.uniplan.course;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.major.Major;
import org.unilab.uniplan.major.MajorService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final MajorService majorService;
    private final CourseMapper courseMapper;
    
    @Autowired
    public CourseService(CourseRepository courseRepository,
                          MajorService majorService,
                          CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.majorService = majorService;
        this.courseMapper = courseMapper;
    }
    @Transactional
    public CourseDTO createCourse(final CourseDTO courseDTO) {
        Major major = majorService.findById(courseDTO.majorId())
                                     .orElseThrow(() ->new RuntimeException("Major with id " + courseDTO.majorId() + " doesn't exist"));
        Course course = new Course(major, courseDTO.courseYear(), courseDTO.courseType() ,
                                   courseDTO.courseSubtype());
        course.setCreatedAt();
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
        Course course  = courseRepository.findById(id)
                                           .orElseThrow(()->new RuntimeException("Course with id " + courseDTO.id() + " doesn't exist"));
        Major major = majorService.findById(courseDTO.majorId())
                                  .orElseThrow(()->new RuntimeException("Major with id " + courseDTO.majorId() + " doesn't exist"));
        course.setMajor(major);
        course.setCourseYear(courseDTO.courseYear());
        course.setCourseType(courseDTO.courseType());
        course.setCourseSubtype(courseDTO.courseSubtype());
        course.setUpdatedAt();
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
