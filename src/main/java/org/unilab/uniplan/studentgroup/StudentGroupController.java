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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.unilab.uniplan.studentgroup.dto.StudentGroupDto;
import org.unilab.uniplan.studentgroup.dto.StudentGroupRequestDto;
import org.unilab.uniplan.studentgroup.dto.StudentGroupResponseDto;

@RestController
@RequestMapping("/student-groups")
@RequiredArgsConstructor
@Tag(name = "Student Groups", description = "Manage the assignment of students to course groups")
public class StudentGroupController {

    private final StudentGroupService studentGroupService;
    private final StudentGroupMapper studentGroupMapper;

    @PostMapping("/add")
    public ResponseEntity<StudentGroupResponseDto> addStudentGroup(@RequestBody @NotNull @Valid final StudentGroupRequestDto studentGroupRequestDTO) {
        final StudentGroupDto studentGroupDTO = studentGroupMapper.toInnerDto(studentGroupRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(studentGroupMapper.toResponseDto(studentGroupService.createStudentGroup(
                                 studentGroupDTO)));
    }

    @GetMapping("/getById")
    public ResponseEntity<StudentGroupResponseDto> getStudentGroupById(@RequestParam @NotNull final UUID studentId,
                                                                       @RequestParam @NotNull final UUID courseGroupId) {

        return ResponseEntity.ok(studentGroupMapper.toResponseDto(studentGroupService
                                                                      .findStudentGroupById(
                                                                          studentId,
                                                                          courseGroupId)
        ));
    }

    @GetMapping("/getAll")
    public List<StudentGroupResponseDto> getAllStudentGroups() {
        return studentGroupMapper.toResponseDtoList(studentGroupService.findAll());
    }

    @PutMapping("/update")
    public ResponseEntity<StudentGroupResponseDto> updateStudentGroup(@RequestParam @NotNull final UUID studentId,
                                                                      @RequestParam @NotNull final UUID courseGroupId,
                                                                      @RequestBody @NotNull @Valid final StudentGroupRequestDto studentGroupRequestDTO) {
        final StudentGroupDto studentGroupDTO = studentGroupMapper.toInnerDto(studentGroupRequestDTO);
        return ResponseEntity.ok(studentGroupMapper
                                     .toResponseDto(studentGroupService.updateStudentGroup(studentId,
                                                                                           courseGroupId,
                                                                                           studentGroupDTO)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteStudentGroup(@RequestParam @NotNull final UUID studentId,
                                                   @RequestParam @NotNull final UUID courseGroupId) {
        studentGroupService.deleteStudentGroup(studentId, courseGroupId);
        return ResponseEntity.noContent().build();
    }
}
