package org.unilab.uniplan.department;

import static org.unilab.uniplan.utils.ErrorConstants.DEPARTMENT_NOT_FOUND;
import static org.unilab.uniplan.utils.ErrorConstants.FACULTY_NOT_FOUND;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.common.model.BaseService;
import org.unilab.uniplan.department.dto.DepartmentDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyRepository;

@Service
@RequiredArgsConstructor
public class DepartmentService implements BaseService<Department> {

    private final DepartmentRepository departmentRepository;


    @Override
    public void save(final Department entity) {
        departmentRepository.save(entity);
    }

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Optional<Department> getById(final UUID id) {
        return departmentRepository.findById(id);
    }

    @Override
    public void delete(final Department entity) {
        departmentRepository.delete(entity);
    }
}