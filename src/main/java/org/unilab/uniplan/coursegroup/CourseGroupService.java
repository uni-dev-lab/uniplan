package org.unilab.uniplan.coursegroup;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseGroupService {
    
    private final CourseGroupRepository courseGroupRepository;
    private final CourseGroupMapper courseGroupMapper;
    
    @Transactional
    public CourseGroupDTO createCourseGroup(final CourseGroupDTO courseGroupDTO) {
        final CourseGroup courseGroup = courseGroupMapper.toEntity(courseGroupDTO);
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
        final CourseGroup courseGroup  = courseGroupRepository.findById(id)
                                         .orElseThrow(() -> new CourseGroupNotFoundException(id));
        courseGroupMapper.updateEntityFromDTO(courseGroupDTO, courseGroup);
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
