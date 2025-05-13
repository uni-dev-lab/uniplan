package org.unilab.uniplan.department;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
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

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

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

        Optional<DepartmentDto> result = departmentService.getDepartmentById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testFindDepartmentByIdShouldReturnEmptyOptionalIfDepartmentNotFound() {
        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        Optional<DepartmentDto> result = departmentService.getDepartmentById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateDepartmentShouldUpdateAndReturnDepartmentDtoIfFound() {
        when(departmentRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(departmentMapper).updateEntityFromDto(dto, entity);
        when(departmentRepository.save(entity)).thenReturn(entity);
        when(departmentMapper.toDto(entity)).thenReturn(dto);

        Optional<DepartmentDto> result = departmentService.updateDepartment(id, dto);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testUpdateDepartmentShouldReturnEmptyOptionalIfDepartmentNotFound() {
        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        Optional<DepartmentDto> result = departmentService.updateDepartment(id, dto);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteDepartmentShouldDeleteDepartmentIfFound() {
        when(departmentRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(departmentRepository).delete(entity);

        assertDoesNotThrow(() -> departmentService.deleteDepartment(id));
        verify(departmentRepository).delete(entity);
    }

    @Test
    void testDeleteDepartmentShouldThrowIfNotFound() {
        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                                                  () -> departmentService.deleteDepartment(id));

        assertTrue(exception.getMessage().contains(id.toString()));

    }
}
