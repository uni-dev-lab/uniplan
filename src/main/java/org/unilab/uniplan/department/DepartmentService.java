package org.unilab.uniplan.department;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public Optional<DepartmentDto> createDepartment(final DepartmentDto departmentDto) {
        final Department department = departmentMapper.toEntity(departmentDto);

        if (department.getDepartmentName() == null || department.getFaculty() == null) {
            return Optional.empty();
        }

        final Department savedDepartment = departmentRepository.save(department);
        return Optional.of(departmentMapper.toDto(savedDepartment));
    }

    public List<DepartmentDto> getAllDepartments() {
        final List<Department> departments = departmentRepository.findAll();
        return departmentMapper.toDtoList(departments);
    }

    public Optional<DepartmentDto> getDepartmentById(final UUID id) {
        final Optional<Department> department = departmentRepository.findById(id);
        return department.map(departmentMapper::toDto);
    }

    @Transactional
    public Optional<DepartmentDto> updateDepartment(final UUID id,
                                                    final DepartmentDto departmentDto) {
        final Optional<Department> existingDepartment = departmentRepository.findById(id);

        if (existingDepartment.isPresent()) {
            final Department department = existingDepartment.get();
            departmentMapper.updateEntityFromDto(departmentDto, department);

            final Department savedDepartment = departmentRepository.save(department);
            return Optional.of(departmentMapper.toDto(savedDepartment));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<DepartmentDto> deleteDepartment(final UUID id) {
        final Optional<Department> departmentOpt = departmentRepository.findById(id);

        if (departmentOpt.isPresent()) {
            final Department department = departmentOpt.get();
            departmentRepository.delete(department);
            return Optional.of(departmentMapper.toDto(department));
        }

        return Optional.empty();
    }
}
