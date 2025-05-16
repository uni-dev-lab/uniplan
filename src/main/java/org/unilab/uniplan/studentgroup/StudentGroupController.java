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

    private static final String STUDENTGROUP_NOT_FOUND = "StudentGroup with ID {0} not found.";

    private final StudentGroupService studentGroupService;
    private final StudentGroupMapper studentGroupMapper;

    @PostMapping
    public ResponseEntity<StudentGroupResponseDTO> addStudentGroup(@RequestBody @NotNull @Valid final StudentGroupRequestDTO studentGroupRequestDTO) {
        final StudentGroupDTO studentGroupDTO = studentGroupMapper.toInnerDTO(studentGroupRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(studentGroupMapper.toResponseDTO(studentGroupService.createStudentGroup(
                                 studentGroupDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentGroupResponseDTO> getStudentGroupById(@PathVariable @NotNull final UUID id) {
        return ResponseEntity.ok(studentGroupMapper.toResponseDTO(studentGroupService.findStudentGroupById(
                                                                                         id)
                                                                                     .orElseThrow(() -> new ResponseStatusException(
                                                                                         HttpStatus.NOT_FOUND,
                                                                                         MessageFormat.format(
                                                                                             STUDENTGROUP_NOT_FOUND,
                                                                                             id)))));
    }

    @GetMapping
    public List<StudentGroupResponseDTO> getAllStudentGroups() {
        return studentGroupMapper.toResponseDTOList(studentGroupService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentGroupResponseDTO> updateStudentGroup(@PathVariable @NotNull final UUID id,
                                                                      @RequestBody @NotNull @Valid final StudentGroupRequestDTO studentGroupRequestDTO) {
        final StudentGroupDTO studentGroupDTO = studentGroupMapper.toInnerDTO(studentGroupRequestDTO);
        return ResponseEntity.ok(studentGroupMapper
                                     .toResponseDTO(studentGroupService.updateStudentGroup(id,
                                                                                           studentGroupDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentGroup(@PathVariable @NotNull final UUID id) {
        studentGroupService.deleteStudentGroup(id);
        return ResponseEntity.noContent().build();
    }
}
