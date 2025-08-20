package org.unilab.uniplan.studentgroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.studentgroup.dto.StudentGroupDto;

@ExtendWith(MockitoExtension.class)
class StudentGroupServiceTest {

    @InjectMocks
    private StudentGroupService studentGroupService;

    @Mock
    private StudentGroupMapper studentGroupMapper;

    @Mock
    private StudentGroupRepository studentGroupRepository;

    private UUID studentId;
    private UUID groupId;
    private StudentGroupId studentGroupId;
    private StudentGroup studentGroup;
    private StudentGroupDto studentGroupDTO;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        groupId = UUID.randomUUID();
        studentGroupId = new StudentGroupId(studentId, groupId);

        studentGroup = new StudentGroup();
        studentGroup.setId(studentGroupId);

        studentGroupDTO = new StudentGroupDto(studentId, groupId);
    }

    @Test
    void createStudentGroupShouldReturnAndSavedStudentGroupDTO() {
        when(studentGroupMapper.toEntity(studentGroupDTO)).thenReturn(studentGroup);
        when(studentGroupRepository.save(studentGroup)).thenReturn(studentGroup);
        when(studentGroupMapper.toDto(studentGroup)).thenReturn(studentGroupDTO);

        StudentGroupDto result = studentGroupService.createStudentGroup(studentGroupDTO);

        assertNotNull(result);
        assertEquals(studentId, result.studentId());
        assertEquals(groupId, result.courseGroupId());
        verify(studentGroupRepository).save(studentGroup);
    }

    @Test
    void updateStudentGroupShouldUpdateAndReturnStudentGroupDTOIfExists() {
        when(studentGroupRepository.findById(studentGroupId)).thenReturn(Optional.of(studentGroup));
        doNothing().when(studentGroupMapper).updateEntityFromDto(studentGroupDTO, studentGroup);
        when(studentGroupRepository.save(studentGroup)).thenReturn(studentGroup);
        when(studentGroupMapper.toDto(studentGroup)).thenReturn(studentGroupDTO);

        StudentGroupDto result = studentGroupService.updateStudentGroup(studentId,
                                                                                  groupId,
                                                                                  studentGroupDTO);

        assertEquals(studentId, result.studentId());
        assertEquals(groupId, result.courseGroupId());
        verify(studentGroupRepository).save(studentGroup);
    }

    @Test
    void updateStudentGroupShouldReturnEmptyIfNotFound() {
        when(studentGroupRepository.findById(studentGroupId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> studentGroupService.updateStudentGroup(
                                                               studentId,
                                                               groupId,
                                                               studentGroupDTO));
        assertTrue(exception.getMessage().contains(String.valueOf(studentGroupId)));
        verify(studentGroupRepository, never()).save(any());
    }

    @Test
    void deleteStudentGroupShouldDeleteIfExists() {
        when(studentGroupRepository.findById(studentGroupId)).thenReturn(Optional.of(studentGroup));

        studentGroupService.deleteStudentGroup(studentId, groupId);

        verify(studentGroupRepository).delete(studentGroup);
    }

    @Test
    void deleteStudentGroupShouldThrowIfNotExists() {
        when(studentGroupRepository.findById(studentGroupId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
            studentGroupService.deleteStudentGroup(studentId, groupId));

        assertTrue(exception.getMessage().contains(String.valueOf(studentGroupId)));
        verify(studentGroupRepository, never()).delete(any());
    }

    @Test
    void findStudentGroupByIdShouldReturnStudentGroupDTOIfFound() {
        when(studentGroupRepository.findById(studentGroupId)).thenReturn(Optional.of(studentGroup));
        when(studentGroupMapper.toDto(studentGroup)).thenReturn(studentGroupDTO);

        StudentGroupDto result = studentGroupService.findStudentGroupById(studentId,
                                                                                    groupId);

        assertEquals(studentId, result.studentId());
        assertEquals(groupId, result.courseGroupId());
    }

    @Test
    void findStudentGroupByIdShouldReturnEmptyIfNotFound() {
        when(studentGroupRepository.findById(studentGroupId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> studentGroupService.findStudentGroupById(
                                                               studentId,
                                                               groupId));

        assertTrue(exception.getMessage().contains(String.valueOf(studentGroupId)));
    }

    @Test
    void findAllShouldReturnListOfStudentGroupDTOs() {
        when(studentGroupRepository.findAll()).thenReturn(List.of(studentGroup));
        when(studentGroupMapper.toDto(studentGroup)).thenReturn(studentGroupDTO);

        List<StudentGroupDto> result = studentGroupService.findAll();

        assertEquals(1, result.size());
        assertEquals(studentId, result.getFirst().studentId());
        assertEquals(groupId, result.getFirst().courseGroupId());
    }
}
