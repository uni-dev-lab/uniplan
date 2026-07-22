package org.unilab.uniplan.coursegroup;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.coursegroup.dto.CourseGroupDto;
import org.unilab.uniplan.exception.CourseGroupNotFoundException;

@Service
@RequiredArgsConstructor
public class CourseGroupService {

    private final CourseGroupRepository courseGroupRepository;
    private final CourseGroupMapper courseGroupMapper;

    @Transactional
    public CourseGroupDto createCourseGroup(final CourseGroupDto courseGroupDTO) {
        final CourseGroup courseGroup = courseGroupMapper.toEntity(courseGroupDTO);
        return saveEntityAndConvertToDto(courseGroup);
    }

    public CourseGroupDto findCourseGroupById(final UUID id) {
        return courseGroupRepository.findById(id)
                                    .map(courseGroupMapper::toDto)
                                    .orElseThrow(() -> new CourseGroupNotFoundException(id));
    }

    public List<CourseGroupDto> findAll() {
        return courseGroupRepository.findAll()
                                    .stream().map(courseGroupMapper::toDto).toList();
    }

    @Transactional
    public CourseGroupDto updateCourseGroup(final UUID id,
                                            final CourseGroupDto courseGroupDTO) {

        return courseGroupRepository.findById(id).map(
                                        existingCourseGroup -> updateEntityAndConvertToDto(courseGroupDTO,
                                                                                           existingCourseGroup))
                                    .orElseThrow(() -> new CourseGroupNotFoundException(id));
    }

    @Transactional
    public void deleteCourseGroup(final UUID id) {
        final CourseGroup courseGroup = courseGroupRepository.findById(id)
                                                             .orElseThrow(
                                                                 () -> new CourseGroupNotFoundException(id));
        courseGroupRepository.delete(courseGroup);
    }

    private CourseGroupDto updateEntityAndConvertToDto(final CourseGroupDto dto,
                                                       final CourseGroup entity) {
        courseGroupMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private CourseGroupDto saveEntityAndConvertToDto(final CourseGroup entity) {
        final CourseGroup savedEntity = courseGroupRepository.save(entity);
        return courseGroupMapper.toDto(savedEntity);
    }
}
