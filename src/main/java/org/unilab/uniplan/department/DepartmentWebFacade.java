package org.unilab.uniplan.department;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.unilab.uniplan.department.dto.DepartmentRequestDto;
import org.unilab.uniplan.department.dto.DepartmentResponseDto;
import org.unilab.uniplan.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;

import static org.unilab.uniplan.utils.ErrorConstants.DEPARTMENT_NOT_FOUND;

@Component
@Slf4j
@RequiredArgsConstructor
public class DepartmentWebFacade {

    private final  DepartmentService departmentService;
    private final  DepartmentValidator departmentValidator;
    private final  DepartmentMapper departmentMapper;

    @Transactional
    public void createDepartment(final DepartmentRequestDto request){
        departmentValidator.validate(request);

        final Department department = departmentMapper.toEntity(request);
        departmentService.save(department);
        log.info("Department with ID {} created", department.getId());
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponseDto> getAllDepartments(){
        return departmentMapper.toResponseDtoList(departmentService.getAll());
    }

    @Transactional(readOnly = true)
    public DepartmentResponseDto getDepartmentById(final UUID id){
        final Department department = getDepartmentOrThrow(id);
        return departmentMapper.toResponseDto(department);
    }

    private Department getDepartmentOrThrow(final UUID id){
        return departmentService.getById(id)
                                .orElseThrow(()->new ResourceNotFoundException(
                                    DEPARTMENT_NOT_FOUND.getMessage(String.valueOf(id))
                                ));
    }

    @Transactional
    public void updateDepartment(final UUID id,
                             final DepartmentRequestDto request) {
        final Department department = getDepartmentOrThrow(id);
        departmentValidator.validate(request);

        departmentMapper.updateEntity(request, department);
        departmentService.save(department);
        log.info("Department with id {} has been updated", department.getId());
    }

    @Transactional
    public void deleteDepartment(final UUID id){
        final Department department = getDepartmentOrThrow(id);
        departmentService.delete(department);
        log.info("Department with id {} has been deleted", department.getId());
    }

}
