package org.unilab.uniplan.department;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.text.MessageFormat;
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
import org.springframework.web.server.ResponseStatusException;
import org.unilab.uniplan.department.dto.DepartmentDto;
import org.unilab.uniplan.department.dto.DepartmentRequestDto;
import org.unilab.uniplan.department.dto.DepartmentResponseDto;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private static final String DEPARTMENT_NOT_FOUND = "Department with ID {0} not found.";

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(
        @Valid @NotNull @RequestBody final DepartmentRequestDto departmentRequestDto) {

        final DepartmentDto departmentDto = departmentService.createDepartment(departmentMapper.toInternalDto(
            departmentRequestDto));

        return new ResponseEntity<>(departmentMapper.toResponseDto(departmentDto),
                                    HttpStatus.CREATED);
    }

    @GetMapping
    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentMapper.toResponseDtoList(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(@NotNull @PathVariable final UUID id) {
        final DepartmentDto departmentDto = departmentService.getDepartmentById(id);

        return ok(departmentMapper.toResponseDto(departmentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(
        @PathVariable final UUID id,
        @Valid @NotNull @RequestBody final DepartmentRequestDto departmentRequestDto) {

        final DepartmentDto departmentDto = departmentMapper.toInternalDto(departmentRequestDto);

        return ok(departmentMapper.toResponseDto(departmentService.updateDepartment(id, departmentDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable final UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
