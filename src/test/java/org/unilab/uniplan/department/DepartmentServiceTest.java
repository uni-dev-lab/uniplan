package org.unilab.uniplan.department;

import static org.assertj.core.api.Assertions.assertThat;
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
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private UUID id;
    private Department entity;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        entity = new Department();
    }

    @Test
    void save_shouldDelegateToRepository_whenCalled() {
        departmentService.save(entity);

        verify(departmentRepository).save(entity);
    }

    @Test
    void getAll_shouldReturnAllDepartments_whenCalled() {
        final var departments = List.of(entity);
        when(departmentRepository.findAll()).thenReturn(departments);

        final var result = departmentService.getAll();

        assertThat(result).isEqualTo(departments);
    }

    @Test
    void getById_shouldReturnEntity_whenDepartmentExists() {
        when(departmentRepository.findById(id)).thenReturn(Optional.of(entity));

        final var result = departmentService.getById(id);

        assertThat(result).contains(entity);
    }

    @Test
    void getById_shouldReturnEmpty_whenDepartmentMissing() {
        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        final var result = departmentService.getById(id);

        assertThat(result).isEmpty();
    }

    @Test
    void delete_shouldDelegateToRepository_whenCalled() {
        departmentService.delete(entity);

        verify(departmentRepository).delete(entity);
    }
}
