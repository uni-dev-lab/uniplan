package org.unilab.uniplan.department;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.unilab.uniplan.department.dto.DepartmentRequestDto;
import org.unilab.uniplan.department.dto.DepartmentResponseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class DepartmentWebFacadeTest {

    @Mock
    private DepartmentService departmentService;

    @Mock
    private DepartmentValidator departmentValidator;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentWebFacade departmentWebFacade;

    private UUID id;
    private UUID facultyId;
    private DepartmentRequestDto request;
    private Department entity;
    private DepartmentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        request = new DepartmentRequestDto(facultyId, "Engineering");
        entity = new Department();
        responseDto = new DepartmentResponseDto(id, facultyId, "Engineering");
    }

    @Test
    void createDepartment_shouldValidateBeforeSaving_whenRequestIsValid() {
        when(departmentMapper.toEntity(request)).thenReturn(entity);

        departmentWebFacade.createDepartment(request);

        final InOrder inOrder = inOrder(departmentValidator, departmentService);
        inOrder.verify(departmentValidator).validate(request);
        inOrder.verify(departmentService).save(entity);
    }

    @Test
    void createDepartment_shouldThrowAndNotSave_whenValidationFails() {
        doThrow(new ResourceNotFoundException("faculty missing"))
            .when(departmentValidator).validate(request);

        assertThatThrownBy(() -> departmentWebFacade.createDepartment(request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(departmentService, never()).save(any());
    }

    @Test
    void getAllDepartments_shouldReturnResponseDtos_whenCalled() {
        final List<Department> entities = List.of(entity);
        final List<DepartmentResponseDto> responseDtos = List.of(responseDto);
        when(departmentService.getAll()).thenReturn(entities);
        when(departmentMapper.toResponseDtoList(entities)).thenReturn(responseDtos);

        final var result = departmentWebFacade.getAllDepartments();

        assertThat(result).isEqualTo(responseDtos);
    }

    @Test
    void getDepartmentById_shouldReturnResponseDto_whenDepartmentExists() {
        when(departmentService.getById(id)).thenReturn(Optional.of(entity));
        when(departmentMapper.toResponseDto(entity)).thenReturn(responseDto);

        final var result = departmentWebFacade.getDepartmentById(id);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void getDepartmentById_shouldThrowResourceNotFoundException_whenDepartmentMissing() {
        when(departmentService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentWebFacade.getDepartmentById(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(String.valueOf(id));
    }

    @Test
    void updateDepartment_shouldValidateThenMapThenSave_whenDepartmentExists() {
        when(departmentService.getById(id)).thenReturn(Optional.of(entity));

        departmentWebFacade.updateDepartment(id, request);

        final InOrder inOrder = inOrder(departmentValidator, departmentMapper, departmentService);
        inOrder.verify(departmentValidator).validate(request);
        inOrder.verify(departmentMapper).updateEntity(request, entity);
        inOrder.verify(departmentService).save(entity);
    }

    @Test
    void updateDepartment_shouldThrowResourceNotFoundException_whenDepartmentMissing() {
        when(departmentService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentWebFacade.updateDepartment(id, request))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(String.valueOf(id));

        verify(departmentService, never()).save(any());
    }

    @Test
    void deleteDepartment_shouldDelete_whenDepartmentExists() {
        when(departmentService.getById(id)).thenReturn(Optional.of(entity));

        departmentWebFacade.deleteDepartment(id);

        verify(departmentService).delete(entity);
    }

    @Test
    void deleteDepartment_shouldThrowResourceNotFoundException_whenDepartmentMissing() {
        when(departmentService.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentWebFacade.deleteDepartment(id))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(String.valueOf(id));

        verify(departmentService, never()).delete(any());
    }
}
