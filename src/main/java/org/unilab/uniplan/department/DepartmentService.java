package org.unilab.uniplan.department;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.department.dto.DepartmentDto;
import org.unilab.uniplan.exception.DepartmentNotFoundException;
import org.unilab.uniplan.exception.FacultyNotFoundException;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyRepository;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final FacultyRepository facultyRepository;

    @Transactional
    public DepartmentDto createDepartment(final DepartmentDto departmentDto) {
        final Department department = departmentMapper.toEntity(departmentDto);

        return saveEntityAndConvertToDto(department);
    }

    public List<DepartmentDto> getAllDepartments() {
        final List<Department> departments = departmentRepository.findAll();

        return departmentMapper.toDtoList(departments);
    }

    public DepartmentDto getDepartmentById(final UUID id) {
        return departmentRepository.findById(id)
                                   .map(departmentMapper::toDto)
                                   .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    @Transactional
    public DepartmentDto updateDepartment(final UUID id,
                                          final DepartmentDto departmentDto) {
        return departmentRepository.findById(id)
                                   .map(existingDepartment -> {
                                       final Faculty faculty = facultyRepository.findById(departmentDto.facultyId())
                                                                                .orElseThrow(
                                                                                    () -> new FacultyNotFoundException(departmentDto.facultyId()));
                                       existingDepartment.setFaculty(faculty);
                                       return updateEntityAndConvertToDto(departmentDto, existingDepartment);
                                   })
                                   .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    @Transactional
    public void deleteDepartment(final UUID id) {
        final Department department = departmentRepository.findById(id)
                                                          .orElseThrow(() -> new DepartmentNotFoundException(id));
        departmentRepository.delete(department);
    }

    private DepartmentDto updateEntityAndConvertToDto(final DepartmentDto dto,
                                                      final Department entity) {
        departmentMapper.updateEntityFromDto(dto, entity);
        return saveEntityAndConvertToDto(entity);
    }

    private DepartmentDto saveEntityAndConvertToDto(final Department entity) {
        final Department savedEntity = departmentRepository.save(entity);
        return departmentMapper.toDto(savedEntity);
    }
}