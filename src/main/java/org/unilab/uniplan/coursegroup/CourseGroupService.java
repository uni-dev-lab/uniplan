package org.unilab.uniplan.coursegroup;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.course.Course;
import org.unilab.uniplan.course.CourseService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseGroupService {
    
    private final CourseGroupRepository courseGroupRepository;
    private final CourseService courseService;
    private final CourseGroupMapper courseGroupMapper;

    @Autowired
    public CourseGroupService(final CourseGroupRepository courseGroupRepository,
                               final CourseService courseService,
                               final CourseGroupMapper courseGroupMapper) {
        this.courseGroupRepository = courseGroupRepository;
        this.courseService = courseService;
        this.courseGroupMapper = courseGroupMapper;
    }

    @Transactional
    public CourseGroupDTO createCourseGroup(final CourseGroupDTO courseGroupDTO) {
        Course course = courseService.findById(courseGroupDTO.courseId())
                                     .orElseThrow(() ->new RuntimeException("Course with id " + courseGroupDTO.courseId() + " doesn't exists"));
        CourseGroup courseGroup = new CourseGroup(course, courseGroupDTO.groupName(), courseGroupDTO.maxGroup());
        courseGroup.setCreatedAt();
        return courseGroupMapper.toDTO(courseGroupRepository.save(courseGroup));
    }

    public Optional<CourseGroupDTO> findCourseGroupById (final UUID id) {
        return courseGroupRepository.findById(id)
                               .map(courseGroupMapper::toDTO);
    }

    public Optional<CourseGroup> findById (final UUID id) {
        return courseGroupRepository.findById(id);
    }

    public List<CourseGroupDTO> findAll () {
        return courseGroupRepository.findAll()
            .stream().map(courseGroupMapper::toDTO).toList();
    }
    @Transactional
    public CourseGroupDTO updateCourseGroup(final UUID id, final CourseGroupDTO courseGroupDTO) {
        CourseGroup courseGroup  = courseGroupRepository.findById(id)
                                         .orElseThrow(()->new RuntimeException("CourseGroup with id " + id + " doesn't exists"));
        Course course = courseService.findById(courseGroupDTO.courseId())
                                     .orElseThrow(() ->new RuntimeException("Course with id " + courseGroupDTO.courseId() + " doesn't exists"));
        courseGroup.setCourse(course);
        courseGroup.setGroupName(courseGroupDTO.groupName());
        courseGroup.setMaxGroup(courseGroupDTO.maxGroup());
        courseGroup.setUpdatedAt();
        return courseGroupMapper.toDTO(courseGroupRepository.save(courseGroup));
    }
    @Transactional
    public boolean deleteCourseGroup(final UUID id){
        if (courseGroupRepository.existsById(id)) {
            courseGroupRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
