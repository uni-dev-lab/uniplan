package org.unilab.uniplan.department;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.department.dto.DepartmentDto;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private static final String DEPARTMENT_NOT_FOUND = "Department with ID {0} not found.";

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public DepartmentDto createDepartment(final DepartmentDto departmentDto) {
        final Department department = departmentMapper.toEntity(departmentDto);

        return saveEntityAndConvertToDto(department);
    }

    public List<DepartmentDto> getAllDepartments() {
        final List<Department> departments = departmentRepository.findAll();

        return departmentMapper.toDtoList(departments);
    }

    public Optional<DepartmentDto> getDepartmentById(final UUID id) {
        return departmentRepository.findById(id)
                                   .map(departmentMapper::toDto);
    }

    @Transactional
    public Optional<DepartmentDto> updateDepartment(final UUID id,
                                                    final DepartmentDto departmentDto) {
        return departmentRepository.findById(id)
                                   .map(existingDepartment -> updateAndSaveEntityAndConvertToDto(
                                       departmentDto,
                                       existingDepartment));
    }

    @Transactional
    public void deleteDepartment(final UUID id) {
        final Department department = departmentRepository.findById(id)
                                                          .orElseThrow(() -> new RuntimeException(
                                                              MessageFormat.format(
                                                                  DEPARTMENT_NOT_FOUND, id)));
        departmentRepository.delete(department);
    }

    private DepartmentDto updateAndSaveEntityAndConvertToDto(final DepartmentDto dto,
                                                             final Department entity) {
        departmentMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private DepartmentDto saveEntityAndConvertToDto(final Department entity) {
        final Department savedEntity = departmentRepository.save(entity);
        return departmentMapper.toDto(savedEntity);
    }
}