package org.unilab.uniplan.department;

import io.swagger.v3.oas.annotations.tags.Tag;
import static org.springframework.http.ResponseEntity.ok;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unilab.uniplan.department.dto.DepartmentDto;
import org.unilab.uniplan.department.dto.DepartmentRequestDto;
import org.unilab.uniplan.department.dto.DepartmentResponseDto;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "Manage university departments (e.g., Computer Systems) associated with faculties")
public class DepartmentController {

    private final DepartmentWebFacade departmentWebFacade;

    @PostMapping
    public ResponseEntity<Void> createDepartment(@Valid @NotNull @RequestBody final DepartmentRequestDto departmentRequestDto) {
        departmentWebFacade.createDepartment(departmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentWebFacade.getAllDepartments());
    }

    @GetMapping("/{id}")
        public ResponseEntity<DepartmentResponseDto> getDepartmentById(@NotNull @PathVariable final UUID id) {
            return ResponseEntity.ok(departmentWebFacade.getDepartmentById(id));
        }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLector(@NotNull @PathVariable final UUID id, @Valid @NotNull @RequestBody final DepartmentRequestDto departmentRequestDto) {
        departmentWebFacade.updateDepartment(id, departmentRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@NotNull @PathVariable final UUID id) {
        departmentWebFacade.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
