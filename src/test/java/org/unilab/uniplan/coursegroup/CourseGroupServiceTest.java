package org.unilab.uniplan.coursegroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.unilab.uniplan.coursegroup.dto.CourseGroupDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class CourseGroupServiceTest {

    @InjectMocks
    private CourseGroupService courseGroupService;

    @Mock
    private CourseGroupMapper courseGroupMapper;

    @Mock
    private CourseGroupRepository courseGroupRepository;

    private CourseGroupDto courseGroupDTO;
    private CourseGroup courseGroup;
    private UUID courseGroupId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        courseGroupId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        courseGroupDTO = new CourseGroupDto(courseGroupId, courseId, "firstGroup", 15);
        courseGroup = new CourseGroup();
    }

    @Test
    void createCourseGroupShouldReturnAndSavedCourseGroupDTO() {
        when(courseGroupMapper.toEntity(courseGroupDTO)).thenReturn(courseGroup);
        when(courseGroupRepository.save(courseGroup)).thenReturn(courseGroup);
        when(courseGroupMapper.toDto(courseGroup)).thenReturn(courseGroupDTO);

        CourseGroupDto result = courseGroupService.createCourseGroup(courseGroupDTO);

        assertEquals(courseGroupDTO, result);
        verify(courseGroupRepository).save(courseGroup);
    }

    @Test
    void findCourseGroupByIdShouldReturnCourseGroupDTOIfNotExists() {
        when(courseGroupRepository.findById(courseGroupId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> courseGroupService.findCourseGroupById(
                                                               courseGroupId));

        assertTrue(exception.getMessage().contains(courseGroupId.toString()));
    }

    @Test
    void findByIdShouldReturnCourseGroupDTOIfExists() {
        when(courseGroupRepository.findById(courseGroupId)).thenReturn(Optional.of(courseGroup));
        when(courseGroupMapper.toDto(courseGroup)).thenReturn(courseGroupDTO);

        CourseGroupDto result = courseGroupService.findCourseGroupById(courseGroupId);

        assertEquals(courseGroupDTO, result);
    }

    @Test
    void findAllShouldReturnListOfCourseGroupDTOs() {
        List<CourseGroup> groupList = List.of(courseGroup);
        when(courseGroupRepository.findAll()).thenReturn(groupList);
        when(courseGroupMapper.toDto(courseGroup)).thenReturn(courseGroupDTO);

        List<CourseGroupDto> result = courseGroupService.findAll();

        assertEquals(1, result.size());
        assertEquals(courseGroupDTO, result.getFirst());
    }

    @Test
    void updateCourseGroupShouldReturnUpdatedCourseGroupDTOIfExists() {
        when(courseGroupRepository.findById(courseGroupId)).thenReturn(Optional.of(courseGroup));
        doNothing().when(courseGroupMapper).updateEntityFromDto(courseGroupDTO, courseGroup);
        when(courseGroupRepository.save(courseGroup)).thenReturn(courseGroup);
        when(courseGroupMapper.toDto(courseGroup)).thenReturn(courseGroupDTO);

        CourseGroupDto result = courseGroupService.updateCourseGroup(courseGroupId,
                                                                               courseGroupDTO);

        assertEquals(courseGroupDTO, result);
        verify(courseGroupRepository).save(courseGroup);
    }

    @Test
    void updateCourseGroupShouldReturnEmptyIfNotFound() {
        when(courseGroupRepository.findById(courseGroupId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> courseGroupService.updateCourseGroup(
                                                               courseGroupId,
                                                               courseGroupDTO));

        assertTrue(exception.getMessage().contains(courseGroupId.toString()));
        verify(courseGroupRepository, never()).save(any());
    }

    @Test
    void deleteCourseGroupShouldDeleteIfExists() {
        when(courseGroupRepository.findById(courseGroupId)).thenReturn(Optional.of(courseGroup));

        courseGroupService.deleteCourseGroup(courseGroupId);

        verify(courseGroupRepository).delete(courseGroup);
    }

    @Test
    void deleteCourseGroupShouldThrowIfNotFound() {
        when(courseGroupRepository.findById(courseGroupId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                  () -> courseGroupService.deleteCourseGroup(
                                                      courseGroupId));

        assertTrue(exception.getMessage().contains(courseGroupId.toString()));
        verify(courseGroupRepository, never()).delete(any());
    }
}
