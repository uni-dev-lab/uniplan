package org.unilab.uniplan.studentgroup;

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

@RestController
@RequestMapping("/studentGroups")
@RequiredArgsConstructor
public class StudentGroupController {

    private static final String STUDENTGROUP_NOT_FOUND = "StudentGroup with StudentID {0} and CourseGroupId {1} not found.";

    private final StudentGroupService studentGroupService;
    private final StudentGroupMapper studentGroupMapper;

    @PostMapping
    public ResponseEntity<StudentGroupResponseDTO> addStudentGroup(@RequestBody @NotNull @Valid final StudentGroupRequestDTO studentGroupRequestDTO) {
        final StudentGroupDTO studentGroupDTO = studentGroupMapper.toInnerDTO(studentGroupRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(studentGroupMapper.toResponseDTO(studentGroupService.createStudentGroup(
                                 studentGroupDTO)));
    }

    @GetMapping("/{studentId}/{courseGroupId}")
    public ResponseEntity<StudentGroupResponseDTO> getStudentGroupById(@PathVariable @NotNull final UUID studentId,
                                                                       @PathVariable @NotNull final UUID courseGroupId) {

        return ResponseEntity.ok(studentGroupMapper.toResponseDTO(studentGroupService
                                                                      .findStudentGroupById(
                                                                          studentId,
                                                                          courseGroupId)
                                                                      .orElseThrow(() -> new ResponseStatusException(
                                                                          HttpStatus.NOT_FOUND,
                                                                          MessageFormat.format(
                                                                              STUDENTGROUP_NOT_FOUND,
                                                                              studentId,
                                                                              courseGroupId)))));
    }

    @GetMapping
    public List<StudentGroupResponseDTO> getAllStudentGroups() {
        return studentGroupMapper.toResponseDTOList(studentGroupService.findAll());
    }

    @PutMapping("/{studentId}/{courseGroupId}")
    public ResponseEntity<StudentGroupResponseDTO> updateStudentGroup(@PathVariable @NotNull final UUID studentId,
                                                                      @PathVariable @NotNull final UUID courseGroupId,
                                                                      @RequestBody @NotNull @Valid final StudentGroupRequestDTO studentGroupRequestDTO) {
        final StudentGroupDTO studentGroupDTO = studentGroupMapper.toInnerDTO(studentGroupRequestDTO);
        return ResponseEntity.ok(studentGroupMapper
                                     .toResponseDTO(studentGroupService.updateStudentGroup(studentId,
                                                                                           courseGroupId,
                                                                                           studentGroupDTO)
                                                                       .orElseThrow(() -> new ResponseStatusException(
                                                                           HttpStatus.NOT_FOUND,
                                                                           MessageFormat.format(
                                                                               STUDENTGROUP_NOT_FOUND,
                                                                               studentId,
                                                                               courseGroupId)))));
    }

    @DeleteMapping("/{studentId}/{courseGroupId}")
    public ResponseEntity<Void> deleteStudentGroup(@PathVariable @NotNull final UUID studentId,
                                                   @PathVariable @NotNull final UUID courseGroupId) {
        studentGroupService.deleteStudentGroup(studentId, courseGroupId);
        return ResponseEntity.noContent().build();
    }
}
