package org.unilab.uniplan.major;

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
import org.unilab.uniplan.major.dto.MajorCoursesResponseDto;
import org.unilab.uniplan.major.dto.MajorDto;
import org.unilab.uniplan.major.dto.MajorRequestDto;
import org.unilab.uniplan.major.dto.MajorResponseDto;

@RestController
@RequestMapping("/majors")
@RequiredArgsConstructor
@Tag(name = "Majors", description = "Manage academic majors (e.g., Informatics, Software Engineering) associated with faculties")
public class MajorController {

    private final MajorService majorService;
    private final MajorMapper majorMapper;

    @PostMapping
    public ResponseEntity<MajorResponseDto> addMajor(@RequestBody @NotNull
                                                     @Valid final MajorRequestDto majorRequestDTO) {
        final MajorDto majorDTO = majorMapper.toInnerDto(majorRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(majorMapper.toResponseDto(majorService.createMajor(majorDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MajorResponseDto> getMajorById(@PathVariable @NotNull final UUID id) {
        return ResponseEntity.ok(majorMapper.toResponseDto(majorService.findMajorById(id)));
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<MajorCoursesResponseDto> getMajorWithCoursesById(@PathVariable @NotNull final UUID id) {
        return ResponseEntity.ok(majorMapper.toFullResponseDto(majorService.findMajorWithCoursesById(id)));
    }

    @GetMapping("/faculty/{facultyId}")
    public  List<MajorResponseDto> getMajorsByFacultyId(@PathVariable @NotNull final UUID facultyId) {
        return majorMapper.toResponseDtoList(majorService.findAllMajorByFacultyId(facultyId));
    }

    @GetMapping("/faculty/{facultyId}/courses")
    public  List<MajorCoursesResponseDto> getMajorsWithCoursesByFacultyId(@PathVariable @NotNull final UUID facultyId) {
        return majorMapper.toFullResponseDtoList(majorService.findAllMajorWithCoursesByFacultyId(facultyId));
    }

    @GetMapping
    public List<MajorResponseDto> getAllMajors() {
        return majorMapper.toResponseDtoList(majorService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MajorResponseDto> updateMajor(@PathVariable @NotNull final UUID id,
                                                        @RequestBody @NotNull @Valid MajorRequestDto majorRequestDTO) {
        final MajorDto majorDTO = majorMapper.toInnerDto(majorRequestDTO);
        return ResponseEntity.ok(majorMapper.toResponseDto(majorService.updateMajor(id, majorDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable @NotNull final UUID id) {
        majorService.deleteMajor(id);
        return ResponseEntity.noContent().build();
    }
}