package org.unilab.uniplan.studentgroup;

import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.unilab.uniplan.studentgroup.dto.StudentGroupDto;
import org.unilab.uniplan.studentgroup.dto.StudentGroupRequestDto;
import org.unilab.uniplan.studentgroup.dto.StudentGroupResponseDto;

@RestController
@RequestMapping("/studentGroups")
@RequiredArgsConstructor
@Tag(name = "Student Groups", description = "Manage the assignment of students to course groups")
public class StudentGroupController {

    private final StudentGroupService studentGroupService;
    private final StudentGroupMapper studentGroupMapper;

    @PostMapping
    public ResponseEntity<StudentGroupResponseDto> addStudentGroup(@RequestBody @NotNull @Valid final StudentGroupRequestDto studentGroupRequestDTO) {
        final StudentGroupDto studentGroupDTO = studentGroupMapper.toInnerDto(studentGroupRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(studentGroupMapper.toResponseDto(studentGroupService.createStudentGroup(
                                 studentGroupDTO)));
    }

    @GetMapping("/{studentId}/{courseGroupId}")
    public ResponseEntity<StudentGroupResponseDto> getStudentGroupById(@PathVariable @NotNull final UUID studentId,
                                                                       @PathVariable @NotNull final UUID courseGroupId) {

        return ResponseEntity.ok(studentGroupMapper.toResponseDto(studentGroupService
                                                                      .findStudentGroupById(
                                                                          studentId,
                                                                          courseGroupId)
        ));
    }

    @GetMapping
    public List<StudentGroupResponseDto> getAllStudentGroups() {
        return studentGroupMapper.toResponseDtoList(studentGroupService.findAll());
    }

    @PutMapping("/{studentId}/{courseGroupId}")
    public ResponseEntity<StudentGroupResponseDto> updateStudentGroup(@PathVariable @NotNull final UUID studentId,
                                                                      @PathVariable @NotNull final UUID courseGroupId,
                                                                      @RequestBody @NotNull @Valid final StudentGroupRequestDto studentGroupRequestDTO) {
        final StudentGroupDto studentGroupDTO = studentGroupMapper.toInnerDto(studentGroupRequestDTO);
        return ResponseEntity.ok(studentGroupMapper
                                     .toResponseDto(studentGroupService.updateStudentGroup(studentId,
                                                                                           courseGroupId,
                                                                                           studentGroupDTO)));
    }

    @DeleteMapping("/{studentId}/{courseGroupId}")
    public ResponseEntity<Void> deleteStudentGroup(@PathVariable @NotNull final UUID studentId,
                                                   @PathVariable @NotNull final UUID courseGroupId) {
        studentGroupService.deleteStudentGroup(studentId, courseGroupId);
        return ResponseEntity.noContent().build();
    }
}
