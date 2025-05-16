package org.unilab.uniplan.coursegroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

@ExtendWith(MockitoExtension.class)
class CourseGroupServiceTest {

    @InjectMocks
    private CourseGroupService courseGroupService;

    @Mock
    private CourseGroupMapper courseGroupMapper;

    @Mock
    private CourseGroupRepository courseGroupRepository;

    private CourseGroupDTO courseGroupDTO;
    private CourseGroup courseGroup;
    private UUID courseGroupId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        courseGroupId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        courseGroupDTO = new CourseGroupDTO(courseGroupId, courseId, "firstGroup", 15);
        courseGroup = new CourseGroup();
    }

    @Test
    void createCourseGroupShouldReturnSavedCourseGroupDTO() {
        when(courseGroupMapper.toEntity(courseGroupDTO)).thenReturn(courseGroup);
        when(courseGroupRepository.save(courseGroup)).thenReturn(courseGroup);
        when(courseGroupMapper.toDTO(courseGroup)).thenReturn(courseGroupDTO);

        CourseGroupDTO result = courseGroupService.createCourseGroup(courseGroupDTO);

        assertEquals(courseGroupDTO, result);
        verify(courseGroupRepository).save(courseGroup);
    }

    @Test
    void findCourseGroupByIdShouldReturnCourseGroupDTOIfNotExists() {
        when(courseGroupRepository.findById(courseGroupId)).thenReturn(Optional.empty());

        Optional<CourseGroupDTO> result = courseGroupService.findCourseGroupById(courseGroupId);

        assertFalse(result.isPresent());
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdShouldReturnCourseGroupDTOIfExists() {
        when(courseGroupRepository.findById(courseGroupId)).thenReturn(Optional.of(courseGroup));
        when(courseGroupMapper.toDTO(courseGroup)).thenReturn(courseGroupDTO);

        Optional<CourseGroupDTO> result = courseGroupService.findCourseGroupById(courseGroupId);

        assertTrue(result.isPresent());
        assertEquals(courseGroupDTO, result.get());
    }

    @Test
    void findAllShouldReturnListOfCourseGroupDTOs() {
        List<CourseGroup> groupList = List.of(courseGroup);
        when(courseGroupRepository.findAll()).thenReturn(groupList);
        when(courseGroupMapper.toDTO(courseGroup)).thenReturn(courseGroupDTO);

        List<CourseGroupDTO> result = courseGroupService.findAll();

        assertEquals(1, result.size());
        assertEquals(courseGroupDTO, result.get(0));
    }

    @Test
    void updateCourseGroupShouldReturnUpdatedCourseGroupDTOIfExists() {
        when(courseGroupRepository.findById(courseGroupId)).thenReturn(Optional.of(courseGroup));
        doNothing().when(courseGroupMapper).updateEntityFromDTO(courseGroupDTO, courseGroup);
        when(courseGroupRepository.save(courseGroup)).thenReturn(courseGroup);
        when(courseGroupMapper.toDTO(courseGroup)).thenReturn(courseGroupDTO);

        Optional<CourseGroupDTO> result = courseGroupService.updateCourseGroup(courseGroupId,
                                                                               courseGroupDTO);

        assertTrue(result.isPresent());
        assertEquals(courseGroupDTO, result.get());
        verify(courseGroupRepository).save(courseGroup);
    }

    @Test
    void updateCourseGroupShouldReturnEmptyIfNotFound() {
        when(courseGroupRepository.findById(courseGroupId)).thenReturn(Optional.empty());

        Optional<CourseGroupDTO> result = courseGroupService.updateCourseGroup(courseGroupId,
                                                                               courseGroupDTO);

        assertFalse(result.isPresent());
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

        RuntimeException exception = assertThrows(RuntimeException.class,
                                                  () -> courseGroupService.deleteCourseGroup(
                                                      courseGroupId));

        assertTrue(exception.getMessage().contains("CourseGroup with ID"));
        verify(courseGroupRepository, never()).delete(any());
    }
}
