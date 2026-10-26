package org.unilab.uniplan.department;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unilab.uniplan.common.model.BaseService;

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