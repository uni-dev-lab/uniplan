package org.unilab.uniplan.department;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
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
import org.unilab.uniplan.department.dto.DepartmentDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyRepository;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private UUID id;
    private UUID facultyId;
    private DepartmentDto dto;
    private Department entity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        dto = new DepartmentDto(id, facultyId, "Engineering");
        entity = new Department();
    }

    @Test
    void testCreateDepartmentShouldSaveAndReturnDepartmentDto() {
        when(departmentMapper.toEntity(dto)).thenReturn(entity);
        when(departmentRepository.save(entity)).thenReturn(entity);
        when(departmentMapper.toDto(entity)).thenReturn(dto);

        DepartmentDto result = departmentService.createDepartment(dto);

        assertEquals(dto, result);
    }

    @Test
    void testAllDepartmentsShouldReturnListOfAllDepartmentDtos() {
        List<Department> departments = List.of(entity);
        List<DepartmentDto> dtos = List.of(dto);

        when(departmentRepository.findAll()).thenReturn(departments);
        when(departmentMapper.toDtoList(departments)).thenReturn(dtos);

        List<DepartmentDto> result = departmentService.getAllDepartments();

        assertEquals(dtos, result);
    }

    @Test
    void testFindDepartmentByIdShouldReturnDepartmentDtoIfFound() {
        when(departmentRepository.findById(id)).thenReturn(Optional.of(entity));
        when(departmentMapper.toDto(entity)).thenReturn(dto);

        DepartmentDto result = departmentService.getDepartmentById(id);

        assertEquals(dto, result);
    }

    @Test
    void testFindDepartmentByIdShouldReturnEmptyOptionalIfDepartmentNotFound() {
        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> departmentService.getDepartmentById(id));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }

    @Test
    void testUpdateDepartmentShouldUpdateAndReturnDepartmentDto() {
        Faculty faculty = new Faculty();

        when(departmentRepository.findById(id)).thenReturn(Optional.of(entity));
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(departmentRepository.save(entity)).thenReturn(entity);
        when(departmentMapper.toDto(entity)).thenReturn(dto);

        DepartmentDto result = departmentService.updateDepartment(id, dto);

        assertEquals(dto, result);
    }

    @Test
    void testUpdateDepartmentShouldThrowIfDepartmentNotFound() {
        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> departmentService.updateDepartment(id, dto));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }

    @Test
    void testUpdateDepartmentShouldThrowIfFacultyNotFound() {
        when(departmentRepository.findById(id)).thenReturn(Optional.of(entity));
        when(facultyRepository.findById(facultyId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> departmentService.updateDepartment(id, dto));

        assertTrue(exception.getMessage().contains(String.valueOf(facultyId)));
    }

    @Test
    void testDeleteDepartmentShouldDeleteDepartmentIfFound() {
        when(departmentRepository.findById(id)).thenReturn(Optional.of(entity));
        doAnswer(invocation -> null).when(departmentRepository).delete(entity);

        assertDoesNotThrow(() -> departmentService.deleteDepartment(id));
        verify(departmentRepository).delete(entity);
    }

    @Test
    void testDeleteDepartmentShouldThrowIfNotFound() {
        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                                                           () -> departmentService.deleteDepartment(id));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }
}