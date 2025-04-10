package org.unilab.uniplan.department;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public DepartmentDto createDepartment(@RequestBody DepartmentDto departmentDto) {
        return departmentService.createDepartment(departmentDto);
    }

    @GetMapping
    public List<DepartmentDto> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Optional<DepartmentDto> getDepartmentById(@PathVariable UUID id) {
        return departmentService.getDepartmentById(id);
    }

    @PutMapping("/{id}")
    public Optional<DepartmentDto> updateDepartment(
        @PathVariable UUID id,
        @RequestBody DepartmentDto departmentDto) {
        return departmentService.updateDepartment(id, departmentDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteDepartment(@PathVariable UUID id) {
        return departmentService.deleteDepartment(id);
    }
}
