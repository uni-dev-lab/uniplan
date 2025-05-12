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

    @Test
    void testCreateDepartmentShouldSaveAndReturnDepartmentDto() {
        UUID id = UUID.randomUUID();
        UUID facultyId = UUID.randomUUID();
        String departmentName = "Engineering";

        DepartmentDto dto = new DepartmentDto(id, facultyId, departmentName);
        Department entity = new Department();
        Department saved = new Department();
        DepartmentDto savedDto = new DepartmentDto(id, facultyId, departmentName);

        when(departmentMapper.toEntity(dto)).thenReturn(entity);
        when(departmentRepository.save(entity)).thenReturn(saved);
        when(departmentMapper.toDto(saved)).thenReturn(savedDto);

        DepartmentDto result = departmentService.createDepartment(dto);

        assertEquals(savedDto, result);
    }

    @Test
    void testAllDepartmentsShouldReturnListOfAllDepartmentDtos() {
        List<Department> departments = List.of(new Department());
        List<DepartmentDto> dtos = List.of(new DepartmentDto(UUID.randomUUID(),
                                                             UUID.randomUUID(),
                                                             "Math"));

        when(departmentRepository.findAll()).thenReturn(departments);
        when(departmentMapper.toDtoList(departments)).thenReturn(dtos);

        List<DepartmentDto> result = departmentService.getAllDepartments();

        assertEquals(dtos, result);
    }

    @Test
    void testFindDepartmentByIdShouldReturnDepartmentDtoIfFound() {
        UUID id = UUID.randomUUID();
        Department department = new Department();
        DepartmentDto dto = new DepartmentDto(id, null, "History");

        when(departmentRepository.findById(id)).thenReturn(Optional.of(department));
        when(departmentMapper.toDto(department)).thenReturn(dto);

        Optional<DepartmentDto> result = departmentService.getDepartmentById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
    }

    @Test
    void testFindDepartmentByIdShouldReturnEmptyOptionalIfDepartmentNotFound() {
        UUID id = UUID.randomUUID();

        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        Optional<DepartmentDto> result = departmentService.getDepartmentById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateDepartmentShouldUpdateAndReturnDepartmentDtoIfFound() {
        UUID id = UUID.randomUUID();
        UUID facultyId = UUID.randomUUID();
        String departmentName = "Science";

        DepartmentDto dto = new DepartmentDto(id, facultyId, departmentName);
        Department existing = new Department();
        Department updated = new Department();
        DepartmentDto updatedDto = new DepartmentDto(id, facultyId, departmentName);

        when(departmentRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(departmentMapper).updateEntityFromDto(dto, existing);
        when(departmentRepository.save(existing)).thenReturn(updated);
        when(departmentMapper.toDto(updated)).thenReturn(updatedDto);

        Optional<DepartmentDto> result = departmentService.updateDepartment(id, dto);

        assertTrue(result.isPresent());
        assertEquals(updatedDto, result.get());
    }

    @Test
    void testUpdateDepartmentShouldReturnEmptyOptionalIfDepartmentNotFound() {
        UUID id = UUID.randomUUID();
        DepartmentDto dto = new DepartmentDto(id, null, "Science");

        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        Optional<DepartmentDto> result = departmentService.updateDepartment(id, dto);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteDepartmentShouldDeleteDepartmentIfFound() {
        UUID id = UUID.randomUUID();
        Department department = new Department();

        when(departmentRepository.findById(id)).thenReturn(Optional.of(department));
        doNothing().when(departmentRepository).delete(department);

        assertDoesNotThrow(() -> departmentService.deleteDepartment(id));
        verify(departmentRepository).delete(department);
    }

    @Test
    void testDeleteDepartmentShouldThrowIfNotFound() {
        UUID id = UUID.randomUUID();

        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class,
                                               () -> departmentService.deleteDepartment(id));

        assertTrue(thrown.getMessage().contains("Department with ID"));
    }
}