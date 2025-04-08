package org.unilab.uniplan.department;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.faculty.Faculty;
import org.unilab.uniplan.faculty.FacultyService;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyService facultyService;
    private final DepartmentMapper departmentMapper;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository,
                             FacultyService facultyService,
                             DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.facultyService = facultyService;
        this.departmentMapper = departmentMapper;
    }

    @Transactional
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Faculty faculty = facultyService.getFaculty(departmentDto.facultyId())
                                        .orElseThrow(() -> new IllegalArgumentException(
                                            "Faculty not found"));

        Department department = departmentMapper.toEntity(departmentDto);
        department.setDepartmentName(departmentDto.departmentName());
        department.setFaculty(faculty);
        department = departmentRepository.save(department);

        return departmentMapper.toDto(department);
    }

    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                          .map(departmentMapper::toDto)
                          .toList();
    }

    public Optional<DepartmentDto> getDepartmentById(UUID id) {
        Optional<Department> department = departmentRepository.findById(id);
        return department.map(departmentMapper::toDto);
    }

    @Transactional
    public Optional<DepartmentDto> updateDepartment(UUID id, DepartmentDto departmentDto) {
        Optional<Department> existingDepartment = departmentRepository.findById(id);

        if (existingDepartment.isPresent()) {
            Faculty faculty = facultyService.getFaculty(departmentDto.facultyId())
                                            .orElseThrow(() -> new IllegalArgumentException(
                                                "Faculty not found"));

            Department department = existingDepartment.get();
            department.setDepartmentName(departmentDto.departmentName());
            department.setFaculty(faculty);

            department = departmentRepository.save(department);
            return Optional.of(departmentMapper.toDto(department));
        }

        return Optional.empty();
    }

    @Transactional
    public boolean deleteDepartment(UUID id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
